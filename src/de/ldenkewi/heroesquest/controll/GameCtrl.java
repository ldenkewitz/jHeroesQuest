package de.ldenkewi.heroesquest.controll;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;

import de.ldenkewi.heroesquest.exceptions.MapLoadException;
import de.ldenkewi.heroesquest.model.*;
import de.ldenkewi.heroesquest.model.map.GameStatus;

/**
 * This singleton class operates with {@link MapCtrl} and {@link ViewCtrl}. <BR>
 * It contains the common logic of the game.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class GameCtrl {
	public 	static final String RESOURCES_MAPS_BASE_FOLDER 	= "resources/maps/base/";
	public 	static final String RESOURCES_MAPS_FOLDER 		= "resources/maps/";
	public 	static final String RESOURCES_SAVE_GAMES_FOLDER = "savegames/";
	private static GameCtrl		gameCtrl;
	private MapLoader 			mapLoader;
	private File				mapFile;

	private MapCtrl mapCtrl = MapCtrl.getInstance();
	
	private GameCtrl() {}
	
	/**
	 * Singleton constructor
	 * @return GameCtrl
	 */
	public static GameCtrl getInstance() 
	  { 
	    if ( gameCtrl == null ) 
	    	gameCtrl = new GameCtrl(); 
	    return gameCtrl; 
	  }

	/**
	 * Main method.
	 * @param args Parameters
	 */
	public static void main(String[] args) {
		// BasicConfigurator replaced with PropertyConfigurator.
	    PropertyConfigurator.configure("log4j.config");
	    ViewCtrl.getInstance().showHeroesQuestFrame();
	}
	
	/** Creates new mapLoader that loads the {@link #mapFile} if it exists.
	 */
	public void initMapLoader() {
		mapLoader = new MapLoader(mapFile);
	}
	
	/** Loads new map.
	 * @param player1 name of first player
	 * @param player2 name of second player
	 */
	protected void loadNewGame(String player1, String player2) {
		mapCtrl.initNewGameValues();
		try{
			mapCtrl.setMap(mapLoader.loadNewMap());
			mapCtrl.setPlayer(player1);
			mapCtrl.setPlayer(player2);
			createGame();
		} catch (MapLoadException mlex) {
			ViewCtrl.getInstance().showInfoBox(mlex.getMessage());
		}
	}
	
	/** Loads a saved map.
	 * @param player1 name of first player
	 * @param player2 name of second player
	 */
	public void loadSavedGame() {
		mapCtrl.initNewGameValues();
		GameStatus gameStatus = null;
		try{
			gameStatus = mapLoader.readGameStatusXml(getMapName());
			mapCtrl.setMap(mapLoader.loadSavedMap(gameStatus));
			mapCtrl.setGameStatus(gameStatus);
			createGame();
		} catch (MapLoadException mlex) {
			ViewCtrl.getInstance().showInfoBox(mlex.getMessage());
		}
	}
	
	/** Creates a new game by the following step:
	 * <li>copies the currently used fields of the map to the {@link MapCtrl}</li> 
	 * <li>creates the gaming field</li> 
	 * <li>sets the heroes on the map if they aren't already, which explore their room/floor automatically</li> 
	 * <li>show start dialog & play starting sound</li>
	 * <li>set first player active, if there is no one currently</li>  
	 * 
	 * @param player1 name of first player
	 * @param player2 name of second player
	 */
	private void createGame() {
		mapCtrl.setFields(mapCtrl.getMap().getFields());
		
		// create gaming field
		ViewCtrl.getInstance().initNewGameUI(mapCtrl.getFields(), mapCtrl.getMap().getSizeX(), 
				mapCtrl.getMap().getSizeY(), mapCtrl.getMap().getMapName(), mapCtrl.getBackgroundFile());
		
		if(mapCtrl.getPlayers().get(0).getFigures().size() == 0) {
			mapCtrl.addHeroesToMap();
		} else {
			mapCtrl.addPlayerFiguresAndObjectsFromSavedGame();
		}
		
		// show start dialog
		SoundCtrl.getInstance().playGameStartSound();
		ViewCtrl.getInstance().showInfoBox("Karte wurde geladen, Runde " 
				+ mapCtrl.getGameStatus().getRoundNumber() +" beginnt.\nViel Spa�");

		// set figure active
		if(mapCtrl.getActiveFigure() == null) {
			setPlayerActive(mapCtrl.getPlayers().get(0));
		}else{
			ViewCtrl.getInstance().setPlayerName(
					mapCtrl.getPlayers().get(mapCtrl.getActivePlayerID()).getName());
			ViewCtrl.getInstance().setActiveFigure(mapCtrl.getActiveFigure());
		}
	}
	
	/**
	 * Rolls out the movePoints of every {@link Figure} of the {@link Player}s dependent on their moveDices.
	 */
	private void rollMoveDices() {
		for (Figure figure : mapCtrl.getPlayers().get(mapCtrl.getActivePlayerID()).getFigures()) {
			figure.setHasMoved(false);
			int points=0;
			for(int i=0; i<2; i++) {
				points = points + (int)(Math.random()*6+1);
			}
			figure.setMovePoints(points);
			figure.setAttackPoints(1);
		}
	}
	
	/**
	 * Gives back the next player in the list and increments the roundNumber in case.
	 * @param index of the active player 
	 * @return the next player
	 */
	private Player nextPlayer(int index) {
		index++;
		Player player = null;
		if(mapCtrl.getPlayers().size() == index) {
			player = mapCtrl.getPlayers().get(0);
			mapCtrl.getGameStatus().setRoundNumber(mapCtrl.getGameStatus().getRoundNumber()+1);
			index = 0;
		}
		player = mapCtrl.getPlayers().get(index);

		if(player.getFigures().size() == 0) {
			ViewCtrl.getInstance().showInfoBox("Player " + player.getName() + " has no active figures.");
			player = nextPlayer(index);
			return player; // return here to do not set a false activePlayerID!
		}
		
		mapCtrl.setActivePlayerID(index);
		return player;
	}

	/**
	 * Sets the given player active.
	 * @param {@link Player} that is set active
	 */
	private void setPlayerActive(Player player) {	
		ViewCtrl.getInstance().setPlayerName(player.getName());
		mapCtrl.setActiveFigure(player.getFigures().get(0)); 
		rollMoveDices();
		ViewCtrl.getInstance().setActiveFigure(mapCtrl.getActiveFigure());
	}

	/**
	 * Sets the next player active and increments the round number in case.
	 */
	public void nextTurn() {
		setPlayerActive(nextPlayer(mapCtrl.getActivePlayerID()));
		ViewCtrl.getInstance().setRoundNr(mapCtrl.getGameStatus().getRoundNumber());
	}
	
	/** Saves the current game.
	 */
	public void saveGame(String fileName) {
		// save the isExplored attribute of all fields 
		mapCtrl.getGameStatus().setFieldExplored(new ArrayList<Boolean>());
		for(int i = 0; i < mapCtrl.getFields().size(); i++) {
			Field field = mapCtrl.getFields().get(i);
			System.out.println(i);
			mapCtrl.getGameStatus().getFieldExplored().add(i, field.isExplored());
		}
		fileName = fileName.substring(0, fileName.lastIndexOf(".xml")); 
		mapLoader.writeGameStatusXml(mapCtrl.getGameStatus(), fileName);
		mapLoader.writeMapXml(mapCtrl.getMap(), fileName);
	}
	
	/** Returns the value of the attribute {@link #mapFile}.
	 * @return the mapFile
	 */
	protected File getMapConfigFile() {
		return mapFile;
	}

	/** Sets the value of the attribute {@link #mapFile}.
	 * @param mapFile the mapFile to set
	 */
	public void setMapFile(File mapFile) {
		this.mapFile = mapFile;
	}
	
	/** Returns the map name.
	 * @return name of the map
	 */
	protected String getMapName() {
		return mapLoader.getMapFileName();
	}
}
