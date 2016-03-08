package de.ldenkewi.heroesquest.view.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import de.ldenkewi.heroesquest.controll.*;
import de.ldenkewi.heroesquest.view.HeroesQuestFrame;
import de.ldenkewi.heroesquest.view.MyFileChooserUI;

public class HeroesQuestActionListener implements ActionListener {

	private HeroesQuestFrame heroesQuestFrame;
	
	public HeroesQuestActionListener(HeroesQuestFrame frame) {
		this.heroesQuestFrame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("HeroesQuestUINew")) {
			ViewCtrl.getInstance().setCorrectMapFormat(false);
			ViewCtrl.getInstance().showNewGameDialog();
		}

		if (e.getActionCommand().equals("HeroesQuestUIExit")) {
			heroesQuestFrame.dispose();
			System.exit(0);
		}

		if (e.getActionCommand().equals("HeroesQuestUIAbout")) {
			ViewCtrl.getInstance().showInfoBox("@author: Lars Denkewitz \n@version: from 14/04/2009");
		}
		
		if (e.getActionCommand().equals("HeroesQuestUILoad")) {
			MyFileChooserUI chooser = new MyFileChooserUI(GameCtrl.RESOURCES_SAVE_GAMES_FOLDER, JFileChooser.OPEN_DIALOG);
			File file = chooser.getFile();
			if ( file == null) { /*do_not_think*/ }
			else if(file.canRead() && file.getName().endsWith(".xml")) {
				GameCtrl.getInstance().setMapFile(file);
				GameCtrl.getInstance().initMapLoader();
				GameCtrl.getInstance().loadSavedGame();
			} else {
				ViewCtrl.getInstance().showInfoBox("Spielstand konnte nicht geladen werden.");
			}
		}
		
		if (e.getActionCommand().equals("HeroesQuestUISave")) {
			MyFileChooserUI chooser = new MyFileChooserUI(GameCtrl.RESOURCES_SAVE_GAMES_FOLDER, JFileChooser.SAVE_DIALOG);
			File file = chooser.getFile();
			if(file != null) {
				String fileName = file.getName();
				try{// check if the file name is valid
					if(!file.exists()) {
						file.createNewFile();
						file.delete();
					}
					GameCtrl.getInstance().saveGame(fileName);
				} catch (IOException ex) {
					ViewCtrl.getInstance().showInfoBox("Dateiname nicht möglich.");
				} catch(Exception ex) {
					ViewCtrl.getInstance().showInfoBox("Spielstand konnte nicht gespeichert werden.");
				}
			}
		}
		
		if (e.getActionCommand().equals("HeroesQuestUIOptions")) {
			ViewCtrl.getInstance().showGameOptionUI();
		}
		
		if (e.getActionCommand().equals("PreviousFigure")) {
			MapCtrl.getInstance().prevFigure();
		}

		if (e.getActionCommand().equals("NextFigure")) {
			MapCtrl.getInstance().nextFigure();
		}

		if (e.getActionCommand().equals("Move")) {
			MapCtrl.getInstance().moveFigure();
		}

		if (e.getActionCommand().equals("Attack")) {
			MapCtrl.getInstance().attackFigure();
		}

		if (e.getActionCommand().equals("LookUp")) {
			MapCtrl.getInstance().setLineOfSight(2);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
		}

		if (e.getActionCommand().equals("LookLeft")) {
			MapCtrl.getInstance().setLineOfSight(3);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
		}

		if (e.getActionCommand().equals("LookRight")) {
			MapCtrl.getInstance().setLineOfSight(1);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
		}

		if (e.getActionCommand().equals("LookDown")) {
			MapCtrl.getInstance().setLineOfSight(0);
			ViewCtrl.getInstance().actualizeFigureSightOnCanvas();
		}

		if (e.getActionCommand().equals("NextTurn")) {
			GameCtrl.getInstance().nextTurn();
		}
		
//		heroesQuestFrame.requestFocus();
	}

}
