package de.ldenkewi.heroesquest.test;

import java.io.File;

import org.junit.Before;

import de.ldenkewi.heroesquest.controll.SoundCtrl;
import de.ldenkewi.heroesquest.controll.ViewCtrl;
import de.ldenkewi.heroesquest.model.map.enums.FigureMapSet;

import junit.framework.TestCase;

public class FigureMapSetTest extends TestCase{
	private String atkSoundFolder, hitSoundFolder, deathSoundFolder;
	private String thumbGraphic, canvasGraphic;
	
	@Before
	public void setUp() {
		this.atkSoundFolder 	= SoundCtrl.SOUND_FOLDER_ATTACK;
		this.hitSoundFolder 	= SoundCtrl.SOUND_FOLDER_HIT;
		this.deathSoundFolder 	= SoundCtrl.SOUND_FOLDER_DEATH;
		this.thumbGraphic 		= ViewCtrl.RESSOURCES_GRAPHICS_FIGURES;
		this.canvasGraphic 		= ViewCtrl.RESSOURCES_GRAPHICS_FIGURES_CANVAS;
	}
	
	public void testIfAllFilesDefinedInEnumDoExist() {
		FigureMapSet figures[] = FigureMapSet.values();
		for (FigureMapSet figureMapSet : figures) {
//			System.out.println("check " + figureMapSet.name());
			assertTrue(new File(atkSoundFolder + figureMapSet.getAtkSoundName()).exists());
			assertTrue(new File(hitSoundFolder + figureMapSet.getHitSoundName()).exists());
			assertTrue(new File(deathSoundFolder + figureMapSet.getDeathSoundName()).exists());
			assertTrue(new File(thumbGraphic + figureMapSet.getThumbGraphic()).exists());
			assertTrue(new File(canvasGraphic + figureMapSet.getThumbGraphic()).exists());
		}
	}
}
