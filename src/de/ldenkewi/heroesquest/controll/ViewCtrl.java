package de.ldenkewi.heroesquest.controll;

import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.ldenkewi.heroesquest.model.*;
import de.ldenkewi.heroesquest.view.*;
import de.ldenkewi.heroesquest.view.listeners.GlobalKeyboardListener;

/**
 * This singleton class acts as the controller between the model and the UI. <br>
 * It has also the job as ActionListener for small components like dialogs.
 * 
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class ViewCtrl implements ActionListener {
	public static final String RESSOURCES_GRAPHICS_FIGURES 		  = "resources/graphics/figures/thumb/";
	public static final String RESSOURCES_GRAPHICS_FIGURES_CANVAS = "resources/graphics/figures/canvas/";
	public final static String RESSOURCES_GRAPHICS_GROUND		  = "resources/graphics/ground/";
	public final static String RESSOURCES_GRAPHICS_MAPITEMS 	  = "resources/graphics/mapitems/";
	public final static String RESSOURCES_GRAPHICS_RANDOM	 	  = "resources/graphics/random/";
	public final static String RESSOURCES_GRAPHICS_ICONS	 	  = "resources/graphics/icons/";
	
	private static ViewCtrl		viewCtrl;
	private HeroesQuestCanvas 	canvas;
	public 	HeroesQuestFrame 	heroesQuestFrame;
	private NewGameDialog 		newGameDialog;
	private GameOptionDialog 	gameOptionDialog;
	private boolean 			isCorrectMapFormat = false;

	private ViewCtrl() { }

	/**
	 * Singleton constructor
	 * @return ViewCtrl
	 */
	public static ViewCtrl getInstance() { 
	    if ( viewCtrl == null ) 
	    	viewCtrl = new ViewCtrl(); 
	    return viewCtrl; 
	}
	
	/**
	 * Initializes the UI like {@link HeroesQuestCanvas} and {@link HeroesQuestFrame}.
	 * 
	 * @param fields
	 * @param sizeX
	 * @param sizeY
	 * @param mapName
	 */
	protected void initNewGameUI(ArrayList<Field> fields, int sizeX, int sizeY, 
			String mapName, File backgroundFile) {
		// TODO allocation problem (beim Einsatz von jME wirds weg sein...)
		if (canvas != null) {
			canvas.removeAll();
			heroesQuestFrame.remove(canvas);
		}
		canvas = null;

		canvas = new HeroesQuestCanvas(new Rectangle(3, 55, heroesQuestFrame.getMaxCanvasWidth(),
				heroesQuestFrame.getMaxCanvasHeight()), fields, sizeX, sizeY, backgroundFile);
		
		heroesQuestFrame.setLblMap(mapName);
		heroesQuestFrame.add(canvas);
		heroesQuestFrame.setComponentsEnabled(true);
		
		heroesQuestFrame.addKeyListener(GlobalKeyboardListener.getInstance());

		
//	if (canvas != null) {
//		
//		canvas.clearCanvas();
//		canvas.init(fields, sizeX, sizeY, backgroundFile);
//	} else {
//
//		canvas = new HeroesQuestCanvas(new Rectangle(3, 55, heroesQuestFrame.getMaxCanvasWidth(),
//				heroesQuestFrame.getMaxCanvasHeight()), fields, sizeX, sizeY, backgroundFile);
//		
//		heroesQuestFrame.add(canvas);
//		heroesQuestFrame.setComponentsEnabled(true);
//		heroesQuestFrame.addKeyListener(GlobalKeyboardListener.getInstance());
//	}
//	heroesQuestFrame.setLblMap(mapName);
		
	}
	
	/**
	 * Creates a new {@link HeroesQuestFrame} window.
	 */
	protected void showHeroesQuestFrame() {
		heroesQuestFrame = new HeroesQuestFrame();
	}
	
	/**
	 * Creates a new {@link FightResultDialog} with the given information and shows it.
	 * 
	 * @param atkF {@link Figure} that attacks
	 * @param defF {@link Figure} that is under attack
	 * @param atkPoints AttackPoints of the atkF
	 * @param defPoints DefensePoints of the defF
	 */
	protected void showFightResult(Figure atkF, Figure defF, int atkPoints, int defPoints) {
		FightResultDialog fr = new FightResultDialog(heroesQuestFrame.getLocation().x,
				heroesQuestFrame.getLocation().y, heroesQuestFrame, atkF, defF, atkPoints, defPoints);
		fr.setVisible(true);
	}
	
	/**
	 * Creates a new {@link NewGameDialog} and shows it.
	 */
	public void showNewGameDialog() {
		newGameDialog = new NewGameDialog(heroesQuestFrame.getLocation().x, heroesQuestFrame.getLocation().y, this,
				heroesQuestFrame);
		newGameDialog.setVisible(true);
	}
	
	/**
	 * Creates a new {@link GameOptionDialog} and shows it.
	 */
	public void showGameOptionUI() {
		gameOptionDialog = new GameOptionDialog(heroesQuestFrame.getLocation().x, heroesQuestFrame.getLocation().y, this,
				heroesQuestFrame);
		gameOptionDialog.setVisible(true);
	}
	
	/**
	 * Creates a {@link javax.swing.JOptionPane} with the given text.
	 * 
	 * @param info
	 *            Text that should be shown.
	 */
	public void showInfoBox(String info) {
		JOptionPane.showMessageDialog(null, info);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		// ***** 					  	  	*****
		// ***** Actions of NewGameDialog 	*****
		// ***** 					  	  	*****
		if (e.getActionCommand().equals("NewGameUIChoose")) {
			MyFileChooserUI chooser = new MyFileChooserUI(GameCtrl.RESOURCES_MAPS_FOLDER, JFileChooser.OPEN_DIALOG);
			File file = chooser.getFile();
			if (file != null && file.getName().endsWith(".xml")) {
				GameCtrl.getInstance().setMapFile(file);
				GameCtrl.getInstance().initMapLoader();
				newGameDialog.setTxtMap(GameCtrl.getInstance().getMapName());
				isCorrectMapFormat = true;
			} else {
				newGameDialog.setTxtMap("Wrong map format");
				isCorrectMapFormat = false;
			}
		}

		if (e.getActionCommand().equals("NewGameUIOk")) {
			if (isCorrectMapFormat) {
				newGameDialog.dispose();
				GameCtrl.getInstance().loadNewGame( newGameDialog.getTxtPlayer1(), 
						newGameDialog.getTxtPlayer2());
			}else
				newGameDialog.dispose();
		}

		if (e.getActionCommand().equals("NewGameUICancel")) {
			newGameDialog.dispose();
		}

		
		// ***** 								*****
		// ***** Actions of GameOptionDialog 	*****
		// ***** 								*****
		if (e.getActionCommand().equals("GameOptionDialogCancel")) {
			gameOptionDialog.dispose();
		}
		
		if (e.getActionCommand().equals("GameOptionDialogOk")) {
			SoundCtrl.setVolume(GameOptionDialog.getVolume());
			gameOptionDialog.dispose();
		}
	}

	

	/**
	 * Adds a figure to the Canvas.
	 * @param figure Figure to be add
	 */
	protected void addFigure(Figure figure) {
		canvas.setFigure(figure);
	}

	/**
	 * Adds a mapItem to the Canvas.
	 * @param mapItemImpl  to be add
	 */
	protected void addMapItemImpl(MapItemImpl mapItemImpl) {
		canvas.setMapItemImpl(mapItemImpl);
	}

	/**
	 * Actualizes {@link HeroesQuestFrame} for the given {@link Figure}.<BR>
	 * @param figure {@link Figure} that is set active
	 */
	protected void actualizeFigureData(Figure figure) {
		heroesQuestFrame.setLblFigure(figure.getFigureMapSetName());
		heroesQuestFrame.setLblAtkDice(String.valueOf(figure.getAttackDices()));
		heroesQuestFrame.setLblAtkPoints(String.valueOf(figure.getAttackPoints()));
		heroesQuestFrame.setLblDefDice(String.valueOf(figure.getDefenseDices()));
		heroesQuestFrame.setLblHealthPoints(String.valueOf(figure.getHealthPoints()));
		heroesQuestFrame.setLblMovePoints(String.valueOf(figure.getMovePoints()));
		heroesQuestFrame.setLblFigurePic(RESSOURCES_GRAPHICS_FIGURES + figure.getTextureFileName());
	}
	
	/** 
	 * Sets the line of sight of the active figure on the canvas.
	 */
	public void actualizeFigureSightOnCanvas() {
		canvas.changeLineOfSight(MapCtrl.getInstance().getActiveFigure());
	}

	/**
	 * Actualizes the UI for the given, moving {@link Figure}.
	 * @param figure {@link Figure} that is moving
	 * @param movePoints movePoint of the {@link Figure} that are left over
	 */
	protected void moveFigure(Figure figure, int movePoints) {
		canvas.moveFigure(figure);
		heroesQuestFrame.setLblMovePoints(String.valueOf(movePoints));
	}
	
	/**
	 * Removes the given {@link Figure} from the canvas.
	 * @param figure {@link Figure} to remove form the canvas
	 */
	protected void deleteFigureFromCanvas(Figure figure) {
		canvas.deleteFigure(figure);
	}
	
	/**
	 * Sets the label for the round number of {@link HeroesQuestFrame}.
	 * @param nr Round number
	 */
	protected void setRoundNr(int nr) {
		heroesQuestFrame.setLblRoundNr(String.valueOf(nr));
	}

	/**
	 * Actualizes the UI for the given {@link Figure}.
	 * @param figure {@link Figure} that is set active
	 */
	protected void setActiveFigure(Figure figure) {
		actualizeFigureData(figure); 			// actualize HeroesQuestFrame
		canvas.setActiveFigure(figure.getId()); // actualize HeroesQuestCanvas
	}

	/**
	 * Sets the label for the player name of {@link HeroesQuestFrame}.
	 * @param name Name of the player
	 */
	protected void setPlayerName(String name) {
		heroesQuestFrame.setLblPlayer(name);
	}
	
	
	/** 
	 * Adds a door between two door-fields.
	 * 
	 * @param x1 x-position of first door-field
	 * @param y1 y-position of first door-field
	 * @param x2 x-position of Second door-field
	 * @param y2 y-position of second door-field
	 */
	protected void setDoorOnCanvas(int x1, int y1, int x2, int y2) {
		canvas.setDoor(x1, y1, x2, y2);
	}

	/** Sets the value of the attribute {@link #isCorrectMapFormat}.
	 * @param isCorrectMapFormat the isCorrectMapFormat to set
	 */
	public void setCorrectMapFormat(boolean isCorrectMapFormat) {
		this.isCorrectMapFormat = isCorrectMapFormat;
	}
}
