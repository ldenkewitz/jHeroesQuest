package de.ldenkewi.heroesquest.model.map;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

import de.ldenkewi.heroesquest.model.*;

/**
 * Class that is used to load and to store all information about a map (*.gmp, *.omp, *.fmp). <BR>
 * It also stores all attributes of each mapItem or figure type like healthPoints, the different dices and so on. <BR>
 * The only exception is the list of {@link #figures}, because every figure that is set active on the playing field, is deleted in this list.<BR>
 * This file has to be changed / extended if there are new objects to be implemented. 
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
@XmlRootElement ( namespace = "http://heroesquest.map.de/" )
@XmlAccessorType( XmlAccessType.PROPERTY )
public class Map {
	private String 				mapName, mapBaseName;
	private ArrayList<Field> 	fields 		= null;
	private ArrayList<Figure> 	figures 	= null;
	private ArrayList<MapItemImpl> 	mapItemImpls 	= null;
	private String 				backgroundFile;
	private int 				sizeX, sizeY;

	public Map() {}
	
	public Map(String mapName, int sizeX, int sizeY, String playerName1, String playerName2) {
		this.mapName 		= mapName;
		this.sizeX			= sizeX;
		this.sizeY			= sizeY;
	}
	
	/** Removes figure from list by given index.
	 * @param index
	 */
	public void removeFigure(int index) {
		figures.remove(index);
	}
	
	/** Removes figure from list by given figure.
	 * @param figure
	 */
	public void removeFigure(Figure figure) {
		figures.remove(figure);
	}
	
	/**
	 * Sets the value of the attribute {@link #fields}.
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	/**
	 * Returns the value of the attribute {@link #fields}.
	 * @return the fields
	 */
	@XmlTransient 
	public ArrayList<Field> getFields() {
		return fields;
	}

	/** Sets the value of the attribute {@link #mapName}.
	 * @param mapName the mapName to set
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	/**
	 * Returns the value of the attribute {@link #mapName}.
	 * @return the mapName
	 */
	public String getMapName() {
		return mapName;
	}
	
	/** Returns the value of the attribute {@link #mapBaseName}.
	 * @return the mapBaseName
	 */
	public String getMapBaseName() {
		return mapBaseName;
	}

	/** Sets the value of the attribute {@link #mapBaseName}.
	 * @param mapBaseName the mapBaseName to set
	 */
	public void setMapBaseName(String mapBaseName) {
		this.mapBaseName = mapBaseName;
	}

	/** Sets the value of the attribute {@link #figures}.
	 * @param figures the figures to set
	 */
	public void setFigures(ArrayList<Figure> figures) {
		this.figures = figures;
	}

	/**
	 * Returns the value of the attribute {@link #figures}.
	 * @return the figures
	 */
	@XmlElementWrapper(name = "figures") 
	@XmlElement(name = "figure") 
	public ArrayList<Figure> getFigures() {
		return figures;
	}

	/** Sets the value of the attribute {@link #mapItemImpls}.
	 * @param mapItemImpls the mapItemImpls to set
	 */
	public void setMapItemImpls(ArrayList<MapItemImpl> mapItemImpls) {
		this.mapItemImpls = mapItemImpls;
	}
	
	/**
	 * Returns the value of the attribute {@link #mapItemImpls}.
	 * @return the mapItemImpls
	 */
	@XmlElementWrapper(name = "mapItems") 
	@XmlElement(name = "mapItem") 
	public ArrayList<MapItemImpl> getMapItemImpls() {
		return mapItemImpls;
	}

	/**
	 * Returns the value of the attribute {@link #sizeX}.
	 * @return the sizeX
	 */
	public int getSizeX() {
		return sizeX;
	}
	
	/** Sets the value of the attribute {@link #sizeX}.
	 * @param sizeX the sizeX to set
	 */
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	/**
	 * Returns the value of the attribute {@link #sizeY}.
	 * @return the sizeY
	 */
	public int getSizeY() {
		return sizeY;
	}

	/** Sets the value of the attribute {@link #sizeY}.
	 * @param sizeY the sizeY to set
	 */
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	/** Returns the value of the attribute {@link #backgroundFile}.
	 * @return the backgroundFile
	 */
	public String getBackgroundFile() {
		return backgroundFile;
	}

	/** Sets the value of the attribute {@link #backgroundFile}.
	 * @param backgroundFile the backgroundFile to set
	 */
	public void setBackgroundFile(String backgroundFile) {
		this.backgroundFile = backgroundFile;
	}
	
}
