package de.ldenkewi.heroesquest.controll;

import java.io.*;
import java.util.ArrayList;

import javax.xml.bind.*;

import de.ldenkewi.heroesquest.exceptions.MapLoadException;
import de.ldenkewi.heroesquest.model.Field;
import de.ldenkewi.heroesquest.model.MapItemImpl;
import de.ldenkewi.heroesquest.model.map.Map;
import de.ldenkewi.heroesquest.model.map.GameStatus;
import de.ldenkewi.heroesquest.model.map.enums.GroundMapSet;
import de.ldenkewi.heroesquest.model.map.enums.ObjectMapSet;

/** This class is used to load the map data from the stored map files. <br>
 * It reads the xml configuration and the map definition files.
 * @author Lars Denkewitz
 */
public class MapLoader {
	private static final String MAP_FILE_GMP  = ".gmp";
	private static final String MAP_GAMES_XML = ".gxml";
	private File 							mapFile;
	private boolean 					initialized = false;
	private ArrayList<Field> 	fields 			= null;
	
	public MapLoader(File file) {
		this.mapFile = file;
		initialized = mapFile.exists();
	}
	
	/**
	 * The method loads {@link #map} and returns it. If problems occur, it returns null. 
	 * @return the map object
	 * 
	 * @throws MapLoadException
	 */
	protected Map loadNewMap() throws MapLoadException{
		if(!initialized) {
			throw new MapLoadException("Map configiguration has not been initialized.");
		}
		this.fields = new ArrayList<Field>();
		Map map = readMapXml();

		readGroundMapFile(map);
		createDoorFields(map);
		
		map.setFields(fields);
		return map;
	}

	/** Calls the default method to load the map object, additionally it set the isExplored 
	 * attribute to the map fields stored in the GameStatus object.  
	 * @param gameStatus
	 * @return the map object
	 */
	protected Map loadSavedMap(GameStatus gameStatus) {
		Map map = loadNewMap();
		
		for(int i = 0; i < map.getFields().size(); i++) {
			Field field = map.getFields().get(i);
			field.setExplored(gameStatus.getFieldExplored().get(i));
			map.getFields().set(i, field);
		}
		
		return map;
	}
	
	/** Goes through the {@link #mapItemImpls} list and sets the hasDoor attribute
	 *  of the field under every door object true.
	 */
	private void createDoorFields(Map map) {
		for (MapItemImpl item : map.getMapItemImpls()) {
			if(item.getObjectMapSetName().equals(ObjectMapSet.DOOR.name())) {
				Field field = fields.get(item.getFieldIndex());
				field.setDoorField(true);
				fields.set(item.getFieldIndex(), field);
			}
		}
	}
	
	/**
	 * Reads the *.gmp mapFile and initializes the ArrayList<Field> {@link #fields} with new {@link Field}
	 * -objects. Field informations that are set: x, y, fieldNumber, textureFileName.
	 * 
	 * @param mapFile *.gmp-mapFile
	 * @return true, if the mapFile has been loaded correctly
	 * @throws MapLoadException
	 */
	private boolean readGroundMapFile(Map map) throws MapLoadException{
		Reader 	reader = null;
		char 	c;
		boolean doRead 	= false;
		int 	row 	= 0; 
		int 	column 	= 0;
		
		try {
			String fileName = GameCtrl.RESOURCES_MAPS_BASE_FOLDER + map.getMapBaseName() + MAP_FILE_GMP;
//			String fileName = mapFile.getAbsolutePath().substring(0,
//					mapFile.getAbsolutePath().lastIndexOf("."))+MAP_FILE_GMP;
			reader = new FileReader(fileName);
			for (int intC; (intC = reader.read()) != -1;) {
				c = (char) intC;
				
				// at every second doRead, increment row
				if (c == '\"') {
					doRead = !doRead;
					if (!doRead) {
						row++;
						column = 0;
					}
					
				// if doRead is true, read every char and increment column
				} else if (doRead) {
					GroundMapSet[] groundTextureSet = GroundMapSet.values();
					fields.add(new Field(column, row, groundTextureSet[intC - 48]));
					column++;
				}
				
				//break condition
				if(row > map.getSizeY() || column > map.getSizeX()) {
					throw new MapLoadException("Difference between the size of groundMap and mapConfig.");
				}
			}
		} catch (FileNotFoundException fnfex) {
			throw new MapLoadException("GroundMap file not found, check its name!", fnfex);
		} catch (IOException ioex) {
			throw new MapLoadException("Error by reading groundMap file.", ioex);
		} finally {
			try {
				reader.close();
			} catch (Exception e) { /*nothing to do*/ }
		}
		return true;
	}
	
	/** Loads a {@link Map} object from the given xml file.
	 */
	private Map readMapXml() {
		Map map = new Map();
		try {
			JAXBContext context = JAXBContext.newInstance( Map.class ); 
			Unmarshaller um = context.createUnmarshaller();
			map = (Map) um.unmarshal( new FileReader(mapFile) );
		} catch(JAXBException jaxbex) {
			throw new MapLoadException("Error by reading the mapConfig.xml with JAXB", jaxbex);
		} catch(FileNotFoundException fnfex) {
			throw new MapLoadException("No Map file found", fnfex);
		}
		return map;
	}
	
	/** Loads a {@link Map} object from the given xml file.
	 */
	protected GameStatus readGameStatusXml(String mapName) {
		GameStatus mapGamesStatus = new GameStatus();
		try {
			JAXBContext context = JAXBContext.newInstance( GameStatus.class ); 
			Unmarshaller um = context.createUnmarshaller();
			mapGamesStatus = (GameStatus) um.unmarshal( new FileReader( GameCtrl.RESOURCES_SAVE_GAMES_FOLDER + mapName + MAP_GAMES_XML) );
		} catch(JAXBException jaxbex) {
			throw new MapLoadException("Error by reading the mapConfig.xml with JAXB", jaxbex);
		} catch(FileNotFoundException fnfex) {
			throw new MapLoadException("No GameStatus file found", fnfex);
		}
		return mapGamesStatus;
	}
	
	/** Writes out the {@link Map} object the a xml file, to save the current game.  
	 */
	protected void writeMapXml(Map map, String fileName) {
		Marshaller m = null;
		try {
			JAXBContext context = JAXBContext.newInstance( Map.class ); 
			m = context.createMarshaller(); 
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
		} catch(Exception e) {
			throw new MapLoadException("Error by writing the Map.xml with JAXB", e);
		}
		Writer w = null; 
		try { 
		  w = new FileWriter( GameCtrl.RESOURCES_SAVE_GAMES_FOLDER + fileName + ".xml" ); 
		  m.marshal( map, w ); 
		}catch (IOException e) { 	throw new MapLoadException("Could not write to map.xml", e); }
		catch (JAXBException e) {	throw new MapLoadException("Error by writing the Map.xml with JAXB", e); } 
		finally { try { w.close(); } catch ( Exception e ) { /* do_not_think */ } }
	}
	
	/** Writes out the {@link GameStatus} object the a xml file, to save the current game.  
	 * @param mapName 
	 */
	protected void writeGameStatusXml(GameStatus gameStatus, String fileName) {
		Marshaller m = null;
		try {
			JAXBContext context = JAXBContext.newInstance( GameStatus.class ); 
			m = context.createMarshaller(); 
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE ); 
		} catch(Exception e) {
			throw new MapLoadException("Error by writing the MapGamesStatus.xml with JAXB", e);
		}
		Writer w = null; 
		try { 
			w = new FileWriter( GameCtrl.RESOURCES_SAVE_GAMES_FOLDER + fileName + MAP_GAMES_XML ); 
			m.marshal( gameStatus, w ); 
		}catch (IOException e) { 	throw new MapLoadException("Could not write to map.xml", e); }
		catch (JAXBException e) {	throw new MapLoadException("Error by writing the MapGamesStatus.xml with JAXB", e); } 
		finally { try { w.close(); } catch ( Exception e ) { /* do_not_think */ } }
	}

	/** 
	 * @return the name of the map file
	 */
	public String getMapFileName() {
		return mapFile.getName().substring(0, mapFile.getName().lastIndexOf(".xml"));
	}
	
	
//	/**
//	 * Reads the map.xml mapFile to initialize {@link #mapConfig}.
//	 * With these informations could be load the other map files. 
//	 * @return true if mapConfig loaded correctly
//	 */
//	private boolean loadMapConfig() {
//		try {
//			JAXBContext context = JAXBContext.newInstance( MapConfig.class ); 
//			Unmarshaller um = context.createUnmarshaller();
//			MapConfig conf = (MapConfig) um.unmarshal( new FileReader(mapFile) );
//			this.mapConfig = conf; 
//		} catch(JAXBException jaxbex) {
//			throw new MapLoadException("Error by reading the mapConfig.xml with JAXB", jaxbex);
//		} catch(FileNotFoundException fnfex) {
//			throw new MapLoadException("No mapConfig file found", fnfex);
//		}
//		return true;
//	}
	
	
//	/**
//	 * Reads the *.omp mapFile and initializes the ArrayList<MapItemImpl> {@link #mapItemImpls} with new
//	 * {@link MapItemImpl} -objects. Field informations that are set: x, y, fieldNumber, textureFileName. It
//	 * also actualizes the {@link #fields} list.
//	 * 
//	 * @param mapFile
//	 *            *.omp-mapFile
//	 * @return true, if the mapFile has been loaded correctly
//	 *
//	 * @throws MapLoadException
//	 */
//	private boolean readObjectMapFile() throws MapLoadException {
//		Field 	field;
//		Reader 	reader = null;
//		
//		char 	c;
//		boolean doRead 	= false;
//		int 	row 	= 0; 
//		int 	column 	= 0;
//		int 	index 	= 0;
//		
//		try {
//			reader = new FileReader(mapFile.getAbsolutePath().substring(0,
//					mapFile.getAbsolutePath().lastIndexOf("."))+".omp");
//			for (int intC; (intC = reader.read()) != -1;) {
//				c = (char) intC;
//				
//				// at every second doRead, increment row
//				if (c == '\"') {
//					doRead = !doRead;
//					if (!doRead) {
//						row++;
//						column = 0;
//					}
//					// if doRead is true, read every char and increment column
//				} else if (doRead) {
//					if (c != '-') {
//						for (ObjectMapSet mapSet : ObjectMapSet.values()) {
//							if(mapSet.getIdentifier() == c) {
//								field = fields.get(index);
//								field.setFree(mapSet.isWalkable());
//								fields.set(index, field);
//								
//								mapItemImpls.add(new MapItemImpl(column, row, mapSet));
//								continue;
//							}
//						}
//					}
//					column++;
//					index++;
//				}
//				
//				//break condition
//				if(row > mapConfig.getSizeY() || column > mapConfig.getSizeX()) {
//					throw new MapLoadException("Difference between the size of objectMap and mapConfig.");
//				}
//			}
//		} catch (FileNotFoundException fnfex) {
//			throw new MapLoadException("ObjectMap file not found, check its name!", fnfex);
//		} catch (IOException ioex) {
//			throw new MapLoadException("Error by reading objectMap file.", ioex);
//		} finally {
//			try {
//				reader.close();
//			} catch (Exception e) { /*nothing to do*/ }
//		}
//		return true;
//	}
	

//	/**
//	 * Reads the *.fmp mapFile and initializes the ArrayList<Figure> {@link #figures} with new
//	 * {@link Figure} -objects. Field informations that are set: x, y, attackDices, defenseDices,
//	 * healthPoints, moveDices, name.
//	 * 
//	 * @param mapFile
//	 *            *.fmp-mapFile
//	 * @return true, if the mapFile has been loaded correctly
//	 * 
//	 * @throws MapLoadException
//	 */
//	private boolean readFigureMapFile() throws MapLoadException {
//		Field 	field;
//		Reader 	reader = null;
//		
//		char 	c;
//		boolean doRead 	= false;
//		int 	row 	= 0; 
//		int 	column 	= 0;
//		int 	index 	= 0;
//		
//		try {
//			reader = new FileReader(mapFile.getAbsolutePath().substring(0,
//					mapFile.getAbsolutePath().lastIndexOf("."))+".fmp");
//			for (int intC; (intC = reader.read()) != -1;) {
//				c = (char) intC;
//				
//				// at every second doRead, increment row
//				if (c == '\"') {
//					doRead = !doRead;
//					if (!doRead) {
//						row++;
//						column = 0;
//					}
//					// if doRead is true, read every char and increment column
//				} else if (doRead) {
//					if (c != '-') {
//						field = fields.get(index);
//						
//						for (FigureMapSet figureMapSet : FigureMapSet.values()) {
//							if(figureMapSet.getIdentifier() == c) {
//								figures.add(new Figure(column, row, index, figureMapSet));
//								continue;
//							}
//						}
//						
//						field.setFree(false);
//						fields.set(index, field);
//					}
//					index++;
//					column++;
//				}
//				
//				//break condition
//				if(row > mapConfig.getSizeY() || column > mapConfig.getSizeX()) {
//					throw new MapLoadException("Difference between the size of figureMap and mapConfig.");
//				}
//			}
//		} catch (FileNotFoundException fnfex) {
//			throw new MapLoadException("FigureMap file not found, check its name!", fnfex);
//		} catch (IOException ioex) {
//			throw new MapLoadException("Error by reading figureMap file.", ioex);
//		} finally {
//			try {
//				reader.close();
//			} catch (Exception e) { /*nothing to do*/ }
//		}
//		return true;
//	}
}
