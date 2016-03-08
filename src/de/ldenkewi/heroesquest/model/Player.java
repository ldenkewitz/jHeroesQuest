package de.ldenkewi.heroesquest.model;

import java.util.ArrayList;

/**
 * Model class of a player.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class Player {
	private String name;
	private ArrayList<Figure> figures;
	
	public Player() { }
	
	public Player(String name) {
		this.name = name;
		this.figures = new ArrayList<Figure>();
	}
	
	/**
	 * Add a {@link Figure} to the list. 
	 * @param figure to be added
	 */
	public void addFigure(Figure figure) {
		figures.add(figure);
	}
	
	/**
	 * Removes a {@link Figure} of the list. 
	 * @param figure to be removed
	 */
	public boolean removeFigure(Figure figure) {
		return figures.remove(figure);
	}

	/** Returns the value of the attribute {@link #name}.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/** Sets the value of the attribute {@link #name}.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Returns the value of the attribute {@link #figures}.
	 * @return the figures
	 */
	public ArrayList<Figure> getFigures() {
		return figures;
	}

	/** Sets the value of the attribute {@link #figures}.
	 * @param figures the figures to set
	 */
	public void setFigures(ArrayList<Figure> figures) {
		this.figures = figures;
	}
}
