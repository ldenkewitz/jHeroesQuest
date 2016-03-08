package de.ldenkewi.heroesquest.controll;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import de.ldenkewi.heroesquest.model.*;
import de.ldenkewi.heroesquest.model.map.GameStatus;
import de.ldenkewi.heroesquest.model.map.Map;
import de.ldenkewi.heroesquest.model.map.enums.ObjectMapSet;

/**
 * This singleton class interacts with {@link Map} and {@link ViewCtrl} to work with the model classes, <BR>
 * {@link Figure}, {@link Field} and {@link Player}. It contains all the logical parts for the game.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class MapCtrl {
	private static MapCtrl 		mapCtrl;
	
	private Map 				map;
	private ArrayList<Field> 	fields;
	private GameStatus			gameStatus;
	
	private MapCtrl() {	}
	// TODO usage of structure or function to control the direction ->turnLeft, turnRight, ...
	/**
	 * Singleton constructor
	 * @return MapCtrl
	 */
	public static MapCtrl getInstance() {
		if( mapCtrl == null )
			mapCtrl = new MapCtrl();
		return mapCtrl;
	}
	
	/**
	 * Sets the class attributes to an initial value (empty). 
	 */
	protected void initNewGameValues() {
		this.fields 		= null;
		this.map 			= null;
		this.gameStatus		= new GameStatus();
	}
	
	/**Adds the starting hero figures on the map an add them to the first players figures. <br> 
	 * By adding, it deletes the figure from original list and explore their start point.
	 */
	protected void addHeroesToMap() {
		for(int i = 0 ; i < map.getFigures().size() ; i++){
			Figure figure = map.getFigures().get(i);
		
			if (figure.isHero()) {
				getPlayers().get(0).addFigure(figure);
				ViewCtrl.getInstance().addFigure(figure);
				map.removeFigure(figure);
				Field field = fields.get(figure.getX() + figure.getY() * 26);
				field.setFree(false);
				fields.set(figure.getX() + figure.getY() * 26, field);
				exploreRoom(field.getFieldNumber(), field, figure.getDirection());
				figure = null;
				i--;
			}
			
		}
	}
	
	/** Adds the active figures of the players to the map and fields. <br>
	 * The method is used by loading a save game.
	 */
	protected void addPlayerFiguresAndObjectsFromSavedGame() {
		// figures
		for (Player player : gameStatus.getPlayers()) {
			for (Figure figure : player.getFigures()) {
				ViewCtrl.getInstance().addFigure(figure);
				Field field = fields.get(figure.getX() + figure.getY() * 26);
				field.setFree(false);
				fields.set(figure.getX() + figure.getY() * 26, field);
			}
		}
		
		for (Field field : fields) {
			if(field.isExplored()) {
				// mapObjects
				addExploredObjectToField(field.getX(), field.getY());

				// Doors
				if(field.isDoorField()) {
					Field nextDoorField = searchDoorNeighbor(field.getX(), field.getY());
					if(!nextDoorField.isExplored() || nextDoorField.getX() < field.getX() || nextDoorField.getY() < field.getY()) {
						addDoor(nextDoorField.getX(), nextDoorField.getY(), 
								field.getX(), field.getY());
						
					}
				}
			}
		}
	}
	
	/**
	 * Adds a door between two door-fields.
	 * 
	 * @param x1 x-position of first door-field
	 * @param y1 y-position of first door-field
	 * @param x2 x-position of Second door-field
	 * @param y2 y-position of second door-field
	 */
	private void addDoor(int x1, int y1, int x2, int y2) {
		int i;
		// the first field shout be the one with the lower index
		if (x1 < x2) {
			i = x2; x2 = x1; x1 = i;
		}
		if (y1 < y2) {
			i = y2; y2 = y1; y1 = i;
		}
		ViewCtrl.getInstance().setDoorOnCanvas(x1, y1, x2, y2);
	}
	
	
	/** Explores recursively the new room, starting from the field and direction that is given. <br>
	 * The algorithm starts at the first field (as default the door field, the hero stepped in), turns one field
	 * to the left and calls this method with the field in this direction again... <br>
	 * After this first left turn, it turns clock wise to the right the next three times after it returns. <br>
	 * With this function the room will be explored effectively and it is not needed any more to have only quadratic rooms.  
	 * @param fieldNr field number of the room
	 * @param field	starting field
	 * @param direction for the algorithm
	 */
	private void exploreRoom(int fieldNr, Field field, int direction) {
		if(field == null || field.getFieldNumber() != fieldNr || field.isExplored()) {
			return;
		}
		exploreField(field);
		
		direction += 2;
		for(int i = 0; i < 4; i++) {
			direction--;			
			if(direction > 3)
				direction = 0;
			else if(direction < 0)
				direction = 3;
			
			exploreRoom(fieldNr, searchFieldInDirection(direction, field.getX(), 
					field.getY()), direction);
		}
		return;
	}
	// TOTO hier weiter machen: Bugfixing beim laden -> letzte bewegung vor neue runde nicht richtig synchronisiert ?!?!?
	private void exploreFloorNew(int fieldNr, Field field, int direction) {
		if(field == null || field.getFieldNumber() != fieldNr || field.isExplored()) {
			return;
		}
		exploreField(field);
		
		// initialize directions
		int leftDirection, rightDirection;
		leftDirection 	= direction + 1; 
		rightDirection 	= direction - 1;
		if(rightDirection < 0)
			rightDirection = 3;
		if(leftDirection > 3)
			leftDirection = 0;
		
		boolean bool = true;
		int rightDepth = 9999, leftDepth = 9999;
		Field newField = field;
		
		do {
			rightDepth = exploreLineInDirection(rightDirection, newField, fieldNr, 0, rightDepth);
			leftDepth  = exploreLineInDirection(leftDirection, newField, fieldNr, 0, leftDepth);
			newField = searchFieldInDirection(direction, newField.getX(), newField.getY());
			if(newField == null || !newField.isExplored() || newField.getFieldNumber() == fieldNr)
				bool = false;
		}
		while(bool);
	}
	
	
	/** Explores all fields of this field type (fieldNr) in direction. Returns the recursion depth.
	 * @param direction
	 * @param field
	 * @param fieldNr
	 */
	private int exploreLineInDirection(int direction, Field field, int fieldNr, int depth, int maxDepth) {
		Field newField = searchFieldInDirection(direction, field.getX(), field.getY());
		if(newField == null || newField.isExplored() || newField.getFieldNumber() != fieldNr || depth > maxDepth) {
			return depth;
		}else{
			System.out.println("field explored:" + newField.getX() + " - " + newField.getY());
			depth++;
			exploreField(field);
			exploreLineInDirection(direction, newField, fieldNr, depth, maxDepth);
		}
		return depth;
	}
	
	/** Adds the object, figure or in case a door to the field.
	 * @param field
	 */
	private void exploreField(Field field) {
		addExploredFigureToField(field.getX(), field.getY());
		addExploredObjectToField(field.getX(), field.getY());
		field.setExplored(true);

		if(field.isDoorField()) {
			Field nf = searchDoorNeighbor(field.getX(), field.getY());
			if(nf != null && !nf.isExplored()) {
				addDoor(field.getX(), field.getY(), nf.getX(), nf.getY());
			}
		}
	}
	
	/**
	 * Finds the figure or mapItem on the field(x,y). If found, the field will be 
	 * setFree(false) and the object will be set to the canvas, it will also be set to the
	 * player2.figure-list and deleted from the map-list.
	 * 
	 * @param x   x-position of the field
	 * @param y   y-position of the field
	 */
	private void addExploredFigureToField(int x, int y) {
		int index = 0;
		for (Figure figure : map.getFigures()) {
			if (figure.getX() == x && figure.getY() == y && !figure.isHero()) {
				ViewCtrl.getInstance().addFigure(figure);
				// player(0) has already all its figures
				gameStatus.getPlayers().get(1).addFigure(figure);
				map.removeFigure(index);
				fields.get(x + y * 26).setFree(false);
				return;
			}
			index++;
		}
	}
	
	/** Finds the mapItem on the field(x,y). If found, the field will be setFree(false) 
	 * and the object will be set to the canvas, and deleted from the map-list.
	 * 
	 * @param x  x-position of the field
	 * @param y  y-position of the field
	 */
	private void addExploredObjectToField(int x, int y) {
		for (MapItemImpl mapItemImpl : map.getMapItemImpls()) {
			if(mapItemImpl.getObjectMapSetName().equals(ObjectMapSet.DOOR.name())) {
				continue;
			}
			if (mapItemImpl.getX() == x && mapItemImpl.getY() == y) {
				ViewCtrl.getInstance().addMapItemImpl(mapItemImpl);
				fields.get(x + y * 26).setFree(false);
			}
		}
	}

	/** 
	 * Checks whether the field, the figure wants to walk can be reached.
	 * @param newField the figure walks to
	 * @return true if the newField is reachable
	 */
	// CheckMovement methode überarbeiten -> block tiefe verringern
	private boolean checkMovement(Field newField) {
		boolean canGo = false;
		Field oldField = null; 
		Figure activeFigure = gameStatus.getActiveFigure();
		
		if(activeFigure.getMovePoints() > 0) { 
			
			if(newField != null) {
				// get the old field from the x,y coordinates if they are on the available playing field
				for (Field f : fields) {
					if(f.getX() == activeFigure.getX() && f.getY() == activeFigure.getY()){
						oldField = f;
					}
				}
				
				if(newField.isFree()) {
					// don´t go through walls (different room numbers)
					if(newField.getFieldNumber() == oldField.getFieldNumber())
						canGo = true;
					// only, if there is a door between these different rooms
					else if(newField.isDoorField() && oldField.isDoorField()){
						// if active figure is a monster it should not cross through closed doors
						if(!newField.isExplored() && !activeFigure.isHero()) {}
						else {
							canGo = true;
						}
					}
				}
			}
		}
		return canGo;
	}
	
	/**
	 * Returns the neighbor door-field of the given field(x,y).
	 * @param x x-position of the field
	 * @param y y-position of the field
	 * @return
	 */
	private Field searchDoorNeighbor(int x, int y) {
		Field f = null;
		if(x >= 0 && fields.get(x-1+y*26).isDoorField()) 
			f = fields.get(x-1+y*26); 
		else if(fields.get(x+1+y*26).isDoorField() && x < map.getSizeX()-1) 
			f = fields.get(x+1+y*26);
		else if(y >= 0 && fields.get(x+(y-1)*26).isDoorField()) 
			f = fields.get(x+(y-1)*26);
		else if(fields.get(x+(y+1)*26).isDoorField() && y < map.getSizeY()-1) 
			f = fields.get(x+(y+1)*26);
		
		return f;
	}

	/**
	 * Returns the next field in the line of sight of the active figure.
	 * @return
	 */
	private Field searchFieldInDirection(int direction, int x, int y) {
		Field newField = null;
		switch (direction) {
			case 0:	y++; break;	//look down
			case 1:	x++; break;	//look right
			case 2:	y--; break;	//look up
			case 3:	x--; break;	//look left
		}
		// field should be in the available borders
		if(x >= 0 && x < map.getSizeX() && y >= 0 && y < map.getSizeY()) {
			for (Field f : fields) {
				if(f.getX() == x && f.getY() == y)
					newField = f;
			}
		}
		return newField;
	}
	
	/**Searches a figure on the given field. Is there is no figure, it returns null.
	 * @param field
	 * @return figure on the field
	 */
	private Figure searchFigureOnField(Field field) {
		Figure figure = null;
		
		if(field != null) {
			for (Player player : gameStatus.getPlayers()) {
				for (Figure f : player.getFigures()) {
					if(f.getX() == field.getX() && f.getY() == field.getY()) 
						figure = f;
				}
			}
		}

		return figure;
	}
	
	/** Removes the figure off the playing field and the players figures list. 
	 * It also plays the death sound of the figure and checks if the active player has won.  
	 * In this case, the win sound will be played and the player gets visual information.  
	 * @param figure
	 */
	private void figureKilled(Figure figure) {
		ViewCtrl.getInstance().deleteFigureFromCanvas(figure);
		SoundCtrl.getInstance().playFigureDied(figure.getDeathSoundName());
		
		for (Player player : gameStatus.getPlayers()) {
			if(player.removeFigure(figure)) {
				
				if(player.getFigures().size() == 0) {
					if(figure.isHero()) {
						SoundCtrl.getInstance().playMonsterPlayerWin();
						ViewCtrl.getInstance().showInfoBox("Das Böse hat gesiegt, Spieler "+
								gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getName()+ " gewinnt.");
					}else{
						if(map.getFigures().size() == 0 && player.getFigures().size() == 0) {
							SoundCtrl.getInstance().playHeroPlayerWin();
							ViewCtrl.getInstance().showInfoBox("Die Helden haben gesiegt, Spieler "+
									gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getName()+ " gewinnt.");
						}
					}
				}
			}
		}
	}
	
	/**
	 * The {@link #activeFigure} attacks the {@link Figure} on the next {@link Field} that is in line of sight of it. <BR>
	 * If there is one, the method checks, if it is an enemy figure, the active one has enough attack points and so on. <BR>
	 * Dependent on the attackDices of the {@link Figure}s, it will roll the dices with a different chance to occur: <BR>
	 * chance to attack = 50% (3/6), chance to defend = 33% (2/6), chance to roll blank = 17% (1/6).  
	 */
	public boolean attackFigure() {
		
		int atkPoints = 0, defPoints = 0, diceCount, lostLive, health;
		Random rnd;
		boolean attacked = false;
		
		Field newField = searchFieldInDirection(gameStatus.getActiveFigure().getDirection(), 
				gameStatus.getActiveFigure().getX(), gameStatus.getActiveFigure().getY());
		
		Figure defFigure = searchFigureOnField(newField);
		
		if(defFigure != null && gameStatus.getActiveFigure().getAttackPoints() > 0 
				&& defFigure.isHero() != gameStatus.getActiveFigure().isHero()) {
			gameStatus.getActiveFigure().setAttackPoints(gameStatus.getActiveFigure().getAttackPoints()-1);
			rnd = new Random();

			// roll the dices
			for (int i = 0; i < gameStatus.getActiveFigure().getAttackDices(); i++) {
				diceCount = rnd.nextInt(6);
				if(diceCount < 3) // chance of 50% to attack
					atkPoints++;
			}
			
			for (int i = 0; i < defFigure.getDefenseDices(); i++) {
				diceCount = rnd.nextInt(6);
				if(diceCount < 2) // chance of 33% to defend
					defPoints++;
			}
			
			SoundCtrl.getInstance().playAttackSound(gameStatus.getActiveFigure().getAttackSoundName());
			ViewCtrl.getInstance().showFightResult(gameStatus.getActiveFigure(), defFigure, atkPoints, defPoints);
			
			lostLive = atkPoints - defPoints;
			if(lostLive <= 0) {
				// figure defended -> defend sound ... ?!
			} else {
				health = defFigure.getHealthPoints()-lostLive;
				if(health <= 0) {
					newField.setFree(true);
					figureKilled(defFigure);
				} else {
					defFigure.setHealthPoints(health);
					SoundCtrl.getInstance().playFigureHit(defFigure.getHitSoundName());
				}
			}
			
			// if figure has moved before the attack, its move points have to set to zero 
			if(gameStatus.getActiveFigure().hasMoved()) {
				gameStatus.getActiveFigure().setMovePoints(0);
			}
			
			ViewCtrl.getInstance().actualizeFigureData(gameStatus.getActiveFigure());
			attacked = true;
		}
		
		return attacked;
	}
	
	/**
	 * Moves the {@link #activeFigure} to the next {@link Field} that is in line of sight of it. <BR>
	 * Before this, it has to be a checking, if the movement is possible, like enough move points and so on.
	 */
	public void moveFigure() {
		Field newField = searchFieldInDirection(gameStatus.getActiveFigure().getDirection(),
				gameStatus.getActiveFigure().getX(),gameStatus.getActiveFigure().getY());
		if(checkMovement(newField)) {
			Field oldField = null;
			
			for (Field f : fields) {
				if(f.getX() == gameStatus.getActiveFigure().getX() && f.getY() == gameStatus.getActiveFigure().getY()){
					oldField = f;
				}
			}

			// if door is crossed
			if(newField.getFieldNumber() != oldField.getFieldNumber()) {
				SoundCtrl.getInstance().playOpenDoorSound();

				// check if new field is new unexplored room 
//				exploreRoom(newField.getX(), newField.getY());
				if(newField.getFieldNumber() != 0)
					exploreRoom(newField.getFieldNumber(), newField, gameStatus.getActiveFigure().getDirection());
			}
			
			System.out.println("bevore floor exploration: " + newField.getX() + " - " +newField.getY());
			
			// if walking on floor, the figure may cross a unexplored corner
			if(newField.getFieldNumber() == 0) {
				exploreFloorNew(newField.getFieldNumber(), newField, gameStatus.getActiveFigure().getDirection());
			}
			
			System.out.println("after floor exploration: " + newField.getX() + " - " +newField.getY());
			gameStatus.getActiveFigure().setX(newField.getX());
			gameStatus.getActiveFigure().setY(newField.getY());
			gameStatus.getActiveFigure().setMovePoints(gameStatus.getActiveFigure().getMovePoints()-1);
			gameStatus.getActiveFigure().setHasMoved(true);
			oldField.setFree(true);
			newField.setFree(false);
			ViewCtrl.getInstance().moveFigure(gameStatus.getActiveFigure(), 
					gameStatus.getActiveFigure().getMovePoints());
		}
	}

	/**
	 * Set {@link #activeFigure} to the next available figure in the figure list of the player.
	 */
	public void nextFigure() {
		int index = gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures().indexOf(gameStatus.getActiveFigure()) + 1;
		if(gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures().size() <= index)
			index = 0;
		gameStatus.setActiveFigure(gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures().get(index));
		if(gameStatus.getActiveFigure().hasMoved())
			gameStatus.getActiveFigure().setMovePoints(0);
		ViewCtrl.getInstance().setActiveFigure(gameStatus.getActiveFigure());
	}
	
	/**
	 * Set {@link #activeFigure} to the previous available figure in the figure list of the player.
	 */
	public void prevFigure() {
		int index = gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures().indexOf(gameStatus.getActiveFigure()) - 1;
		if(index < 0) {
			// take the last figure in the list ( index = size - 1 )
			gameStatus.setActiveFigure(gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures()
					.get(gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures().size()-1));
		} else {
			gameStatus.setActiveFigure(gameStatus.getPlayers().get(gameStatus.getActivePlayerID()).getFigures().get(index));
		}
		if(gameStatus.getActiveFigure().hasMoved())
			gameStatus.getActiveFigure().setMovePoints(0);
		ViewCtrl.getInstance().setActiveFigure(gameStatus.getActiveFigure());
	}
	
	/** Sets the value of the attribute {@link #map}.
	 * @param map the map to set
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/** Returns the value of the attribute {@link #map}.
	 * @return the map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Sets the line of sight of the active figure.
	 * @param x line of sight on x-axis (1 or -1)
	 * @param y line of sight on y-axis (1 or -1)
	 */
	public void setLineOfSight(int direction) {
		gameStatus.getActiveFigure().setDirection(direction);
	}
	
	/** Sets the value of the attribute {@link #fields}.
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}
	
	/** Returns the value of the attribute {@link #fields}.
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}
	
	/**
	 * Sets new Player to players ArrayList.
	 * @param name of the player
	 */
	public void setPlayer(String name) {
		if(name.equals(""))
			name = "Spieler"+(gameStatus.getPlayers().size()+1);
		gameStatus.getPlayers().add(new Player(name));
	}
	
	/** Sets the value of the attribute {@link #players}.
	 * @param players the players to set
	 */
	public void setPlayers(ArrayList<Player> players) {
		gameStatus.setPlayers(players);
	}
	
	/** Returns the value of the attribute {@link #players}.
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return gameStatus.getPlayers();
	}
	
	/** Sets the value of the attribute {@link #activePlayerID}.
	 * @param activePlayerID the activePlayerID to set
	 */
	public void setActivePlayerID(int activePlayerID) {
		gameStatus.setActivePlayerID(activePlayerID);
	}

	/** Returns the value of the attribute {@link #activePlayerID}.
	 * @return the activePlayerID
	 */
	public int getActivePlayerID() {
		return gameStatus.getActivePlayerID();
	}

	/** Sets the value of the attribute {@link #activeFigure}.
	 * @param activeFigure the activeFigure to set
	 */
	public void setActiveFigure(Figure activeFigure) {
		gameStatus.setActiveFigure(activeFigure);
	}
	
	/**
	 * Returns the active figure.
	 * @return {@link #activeFigure}
	 */
	public Figure getActiveFigure() {
		return gameStatus.getActiveFigure();
	}
	
	/** Returns the file of the background graphic of the map. <br>
	 * If there is no background file existing, it will load the default background.
	 * @return background graphic file
	 */
	public File getBackgroundFile() {
		File file = new File(GameCtrl.RESOURCES_MAPS_BASE_FOLDER + map.getBackgroundFile());
		if(file == null || !file.exists()) {
			file = new File(ViewCtrl.RESSOURCES_GRAPHICS_RANDOM + "castle.jpg");
		}
		return file;
	}

	/** Returns the value of the attribute {@link #gameStatus}.
	 * @return the gameStatus
	 */
	public GameStatus getGameStatus() {
		return gameStatus;
	}

	/** Sets the value of the attribute {@link #gameStatus}.
	 * @param gameStatus the gameStatus to set
	 */
	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

}
