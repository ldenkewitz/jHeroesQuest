package de.ldenkewi.heroesquest.model;

import de.ldenkewi.heroesquest.model.map.enums.GroundMapSet;

/**
 * Model class of a field. It stores information of a single field like position, texture and so on. 
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class Field {
	private int x, y;
	private String groundMapSetName;
	private boolean isExplored, isFree, isDoorField;
	
	public Field() {}
	
	public Field(int x, int y, GroundMapSet groundMapSet) {
		this.x = x;
		this.y = y;
		this.groundMapSetName = groundMapSet.name();
		this.isFree 	 = true;
		this.isExplored  = false;
		this.isDoorField = false;
	}
	
	/** Returns the value of the attribute {@link #isExplored}.
	 * @return the isExplored
	 */
	public boolean isExplored() {
		return isExplored;
	}

	/** Sets the value of the attribute {@link #isExplored}.
	 * @param isExplored the isExplored to set
	 */
	public void setExplored(boolean isExplored) {
		this.isExplored = isExplored;
	}

	/** Returns the value of the attribute {@link #isFree}.
	 * @return the isFree
	 */
	public boolean isFree() {
		return isFree;
	}

	/** Sets the value of the attribute {@link #isFree}.
	 * @param isFree the isFree to set
	 */
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	/** Returns the value of the attribute {@link #isDoorField}.
	 * @return the isDoorField
	 */
	public boolean isDoorField() {
		return isDoorField;
	}

	/** Sets the value of the attribute {@link #isDoorField}.
	 * @param isDoorField the isDoorField to set
	 */
	public void setDoorField(boolean hasDoor) {
		this.isDoorField = hasDoor;
	}

	/** Returns the value of the attribute {@link #x}.
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/** Sets the value of the attribute {@link #x}.
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/** Returns the value of the attribute {@link #y}.
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/** Sets the value of the attribute {@link #y}.
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/** Returns the value of the attribute {@link #groundMapSetName}.
	 * @return the groundMapSetName
	 */
	public String getGroundMapSetName() {
		return groundMapSetName;
	}

	/** Sets the value of the attribute {@link #groundMapSetName}.
	 * @param groundMapSetName the groundMapSetName to set
	 */
	public void setGroundMapSetName(String groundMapSetName) {
		this.groundMapSetName = groundMapSetName;
	}
	
	/** Returns the value of the attribute {@link #fieldNumber}.
	 * @return the fieldNumber
	 */
	public int getFieldNumber() {
		return GroundMapSet.valueOf(groundMapSetName).getIndex();
	}
}
