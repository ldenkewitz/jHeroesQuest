package de.ldenkewi.heroesquest.model.map.enums;

/**
 * Set of textures of the ground map
 * @author Lars Denkewitz
 *
 */
public enum GroundMapSet {
	//		  graphicName	 Fnr
	GROUND_0 ("ground0.jpg", 0),
	GROUND_1 ("ground1.jpg", 1),
	GROUND_2 ("ground2.jpg", 2),
	GROUND_3 ("ground3.jpg", 3),
	GROUND_4 ("ground4.jpg", 4), 
	GROUND_5 ("ground5.jpg", 5), 
	GROUND_6 ("ground6.jpg", 6), 
	GROUND_7 ("ground7.jpg", 7);
	
	private String fileName;
	private int index;
	
	private GroundMapSet(String fileName, int index) {
		this.fileName = fileName;
		this.index = index;
	}

	/** Returns the value of the attribute {@link #fileName}.
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/** Returns the value of the attribute {@link #index}.
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
}
