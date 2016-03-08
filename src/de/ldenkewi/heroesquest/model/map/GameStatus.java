package de.ldenkewi.heroesquest.model.map;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import de.ldenkewi.heroesquest.model.Figure;
import de.ldenkewi.heroesquest.model.Player;

@XmlRootElement( namespace = "http://heroesquest.map.save.de/" )
public class GameStatus {
	private ArrayList<Player> 	players			=  new ArrayList<Player>();
	private ArrayList<Boolean>	fieldExplored 	=  new ArrayList<Boolean>();
	private int					roundNumber		= 1;
	private int 				activePlayerID;
	private Figure 				activeFigure;
	
	public GameStatus() {
		
	}
	
	/** Returns the value of the attribute {@link #players}.
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/** Sets the value of the attribute {@link #players}.
	 * @param players the players to set
	 */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	/** Returns the value of the attribute {@link #activeFigure}.
	 * @return the activeFigure
	 */
	public Figure getActiveFigure() {
		return activeFigure;
	}

	/** Sets the value of the attribute {@link #activeFigure}.
	 * @param activeFigure the activeFigure to set
	 */
	public void setActiveFigure(Figure activeFigure) {
		this.activeFigure = activeFigure;
	}

	/** Returns the value of the attribute {@link #activePlayerID}.
	 * @return the activePlayerID
	 */
	public int getActivePlayerID() {
		return activePlayerID;
	}

	/** Sets the value of the attribute {@link #activePlayerID}.
	 * @param activePlayerID the activePlayerID to set
	 */
	public void setActivePlayerID(int activePlayerID) {
		this.activePlayerID = activePlayerID;
	}

	/** Returns the value of the attribute {@link #roundNumber}.
	 * @return the roundNumber
	 */
	public int getRoundNumber() {
		return roundNumber;
	}

	/** Sets the value of the attribute {@link #roundNumber}.
	 * @param roundNumber the roundNumber to set
	 */
	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	/** Returns the value of the attribute {@link #fieldExplored}.
	 * @return the fieldExplored
	 */
	public ArrayList<Boolean> getFieldExplored() {
		return fieldExplored;
	}

	/** Sets the value of the attribute {@link #fieldExplored}.
	 * @param fieldExplored the fieldExplored to set
	 */
	public void setFieldExplored(ArrayList<Boolean> fieldExplored) {
		this.fieldExplored = fieldExplored;
	}
	
}
