package de.ldenkewi.heroesquest.model.map.enums;

/**
 * Set of textures of the object map
 * @author Lars Denkewitz
 *
 */
public enum ObjectMapSet {
	//		 graphicName	Id	 walk
	TABLE 	("table.jpg", 	'T', false),
//	CLOSET 	("closet.jpg", 	'C', false),
	ROCK 	("rock.jpg", 	'R', false),
	STAIRS 	("stairs.jpg", 	'S', false),
	DOOR	("door.jpg", 	'D', true);
	
	private String fileName;
	private char identifier;
	private boolean isWalkable;
	
	private ObjectMapSet(String fileName, char identifier, boolean isWalkable) {
		this.fileName = fileName;
		this.identifier = identifier;
		this.isWalkable = isWalkable;
	}

	/** Returns the value of the attribute {@link #fileName}.
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/** Returns the value of the attribute {@link #identifier}.
	 * @return the identifier
	 */
	public char getIdentifier() {
		return identifier;
	}

	/** Returns the value of the attribute {@link #isWalkable}.
	 * @return the isWalkable
	 */
	public boolean isWalkable() {
		return isWalkable;
	}
}
