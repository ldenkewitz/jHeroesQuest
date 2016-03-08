package de.ldenkewi.heroesquest.model;

abstract public class MapItem {
	private int x, y;
	private int sizeX 		= 1;
	private int sizeY 		= 1;
	private int direction	= 0;
	//direction: S=0, E=1, N=2, W=3
	
	public MapItem() {}
	
	public MapItem(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public MapItem(int x, int y, int sizeX, int sizeY, int direction) {
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.direction = direction;
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

	/** Returns the value of the attribute {@link #sizeX}.
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

	/** Returns the value of the attribute {@link #sizeY}.
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

	/** Returns the value of the attribute {@link #lineOfSightY}.
	 * @return the lineOfSightY
	 */
	public int getDirection() {
		return direction;
	}

	/** Sets the value of the attribute {@link #lineOfSightY}.
	 * @param lineOfSightY the lineOfSightY to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/** Returns the value of the attribute {@link #textureFileName}.
	 * @return the textureFileName
	 */
	abstract public String getTextureFileName();
}
