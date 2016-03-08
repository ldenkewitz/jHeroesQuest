package de.ldenkewi.heroesquest.model;

import de.ldenkewi.heroesquest.model.map.enums.ObjectMapSet;

/**
 * Model class of a map item. It stores information about static map objects like tables, stones and so on.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class MapItemImpl extends MapItem{
	private String objectMapSetName; 

	public MapItemImpl() {}
	
	public MapItemImpl(int x, int y, ObjectMapSet objectMapSet) {
		super(x, y);
		this.objectMapSetName = objectMapSet.name();
	}

	@Override
	public String getTextureFileName() {
		return ObjectMapSet.valueOf(objectMapSetName).getFileName();
	}

	/** Returns the value of the attribute {@link #objectMapSetName}.
	 * @return the objectMapSetName
	 */
	public String getObjectMapSetName() {
		return objectMapSetName;
	}

	/** Sets the value of the attribute {@link #objectMapSetName}.
	 * @param objectMapSetName the objectMapSetName to set
	 */
	public void setObjectMapSetName(String objectMapSetName) {
		this.objectMapSetName = objectMapSetName;
	}

	/** Returns the value of the attribute {@link #fieldIndex}.
	 * @return the fieldIndex
	 */
	public int getFieldIndex() {
		return (getY() * 26 + getX());
	}
	
}
