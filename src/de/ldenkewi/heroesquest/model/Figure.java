package de.ldenkewi.heroesquest.model;

import de.ldenkewi.heroesquest.model.map.enums.FigureMapSet;

/**
 * Model class of a figure. It stores information of a figure like move- or attack dices, health points and so on. <BR>
 * It extends the class {@link MapItemImpl} that contains primary information like the position of every object on the map. <BR>
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class Figure extends MapItem {
	private int healthPoints, movePoints, attackPoints;
	private String figureMapSetName;
	private boolean hasMoved;
	private int id;
	
	public Figure() {
		super(0, 0);
	}
	
	public Figure(int column, int row, int id, FigureMapSet figureMapSet) {
		super(column, row);
		this.id = id;
		this.figureMapSetName = figureMapSet.name();
		this.healthPoints = figureMapSet.getHealth();
		this.attackPoints = 1;
	}

	/** Returns the value of the attribute {@link #healthPoints}.
	 * @return the healthPoints
	 */
	public int getHealthPoints() {
		return healthPoints;
	}

	/** Sets the value of the attribute {@link #healthPoints}.
	 * @param healthPoints the healthPoints to set
	 */
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}

	/** Returns the value of the attribute {@link #movePoints}.
	 * @return the movePoints
	 */
	public int getMovePoints() {
		return movePoints;
	}

	/** Sets the value of the attribute {@link #movePoints}.
	 * @param movePoints the movePoints to set
	 */
	public void setMovePoints(int movePoints) {
		this.movePoints = movePoints;
	}

	/** Returns the value of the attribute {@link #attackPoints}.
	 * @return the attackPoints
	 */
	public int getAttackPoints() {
		return attackPoints;
	}

	/** Sets the value of the attribute {@link #attackPoints}.
	 * @param attackPoints the attackPoints to set
	 */
	public void setAttackPoints(int attackPoints) {
		this.attackPoints = attackPoints;
	}

	/** Returns the value of the attribute {@link #hasMoved}.
	 * @return the hasMoved
	 */
	public Boolean hasMoved() {
		return hasMoved;
	}

	/** Sets the value of the attribute {@link #hasMoved}.
	 * @param hasMoved the hasMoved to set
	 */
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	/** Returns the value of the attribute {@link #hasMoved}.
	 * @return the hasMoved
	 */
	public boolean isHasMoved() {
		return hasMoved;
	}

	/** Returns the value of the attribute {@link #id}.
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/** Sets the value of the attribute {@link #id}.
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/** Returns the value of the attribute {@link #figureMapSetName}, the name of the figure.
	 * @return the figureMapSetName
	 */
	public String getFigureMapSetName() {
		return figureMapSetName;
	}
	
	/** Sets the value of the attribute {@link #figureMapSetName}.
	 * @param figureMapSetName the figureMapSetName to set
	 */
	public void setFigureMapSetName(String figureMapSetName) {
		this.figureMapSetName = figureMapSetName;
	}

	/** Returns the value of the attribute moveDices from {@link FigureMapSet}.
	 * @return the moveDices
	 */
	public int getMoveDices() {
		return FigureMapSet.valueOf(figureMapSetName).getMoveDices();
	}

	/** Returns the value of the attribute attackDices from {@link FigureMapSet}.
	 * @return the attackDices
	 */
	public int getAttackDices() {
		return FigureMapSet.valueOf(figureMapSetName).getAttackDices();
	}

	/** Returns the value of the attribute defenceDices from {@link FigureMapSet}.
	 * @return the defenseDices
	 */
	public int getDefenseDices() {
		return FigureMapSet.valueOf(figureMapSetName).getDefenseDices();
	}

	/** Returns the value of the attribute isHero from {@link FigureMapSet}.
	 * @return the isHero
	 */
	public boolean isHero() {
		return FigureMapSet.valueOf(figureMapSetName).isHero();
	}

	/** Returns the value of the attribute thumbGraphic from {@link FigureMapSet}.
	 * @see de.ldenkewi.heroesquest.model.MapItem#getTextureFileName()
	 */
	@Override
	public String getTextureFileName() {
		return FigureMapSet.valueOf(figureMapSetName).getThumbGraphic();
	}

	/** Returns the value of the attribute atkSoundName from {@link FigureMapSet}.
	 * @return
	 */
	public String getAttackSoundName() {
		return FigureMapSet.valueOf(figureMapSetName).getAtkSoundName();
	}
	
	/** Returns the value of the attribute deathSoundName from {@link FigureMapSet}.
	 * @return
	 */
	public String getDeathSoundName() {
		return FigureMapSet.valueOf(figureMapSetName).getDeathSoundName();
	}
	
	/** Returns the value of the attribute hitSoundName from {@link FigureMapSet}.
	 * @return
	 */
	public String getHitSoundName() {
		return FigureMapSet.valueOf(figureMapSetName).getHitSoundName();
	}
}
