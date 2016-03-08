package de.ldenkewi.heroesquest.view.listeners;

import java.awt.event.*;

import de.ldenkewi.heroesquest.controll.*;


/**
 * Singleton class that for providing keyboard control to the game, extending KeyAdapter.
 * @author Lars Denkewitz
 *
 */
public class GlobalKeyboardListener extends KeyAdapter {
	private static GlobalKeyboardListener globalKeyboardListener;
	
	private GlobalKeyboardListener() { }

	/**
	 * Singleton constructor
	 * @return ViewCtrl
	 */
	public static GlobalKeyboardListener getInstance() { 
	    if ( globalKeyboardListener == null ) 
	    	globalKeyboardListener = new GlobalKeyboardListener(); 
	    return globalKeyboardListener; 
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			MapCtrl.getInstance().setLineOfSight(2);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
			MapCtrl.getInstance().moveFigure();
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			MapCtrl.getInstance().setLineOfSight(3);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
			MapCtrl.getInstance().moveFigure();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			MapCtrl.getInstance().setLineOfSight(1);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
			MapCtrl.getInstance().moveFigure();
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			MapCtrl.getInstance().setLineOfSight(0);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
			MapCtrl.getInstance().moveFigure();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			MapCtrl.getInstance().attackFigure();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			MapCtrl.getInstance().nextFigure();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			MapCtrl.getInstance().prevFigure();
		}
	}
}
