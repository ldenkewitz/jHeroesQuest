package de.ldenkewi.heroesquest.controll;

import java.io.*;

import javax.sound.sampled.*;
import javax.sound.sampled.FloatControl.Type;

import de.ldenkewi.heroesquest.exceptions.GameSoundException;


/** This singleton class is concerned with the sound of the game.
 * @author Lars Denkewitz
 *
 */
public class SoundCtrl {
	private static SoundCtrl soundCtrl;
	private static float 	 volume = -5.0f;
	
	public static final String SOUND_FOLDER_GAMESTART 	= "resources/sound/gameStart/";
	public static final String SOUND_FOLDER_GAMEEND 	= "resources/sound/gameEnd/";
	public static final String SOUND_FOLDER_OPENDOOR	= "resources/sound/openDoor/";
	public static final String SOUND_FOLDER_ATTACK 		= "resources/sound/fight/attack/";
	public static final String SOUND_FOLDER_HIT 		= "resources/sound/fight/hit/";
	public static final String SOUND_FOLDER_DEATH		= "resources/sound/fight/death/";
	
	private SoundCtrl() {}
	
	/**
	 * Singleton constructor
	 * @return GameCtrl
	 */
	public static SoundCtrl getInstance() 
	{ 
	    if ( soundCtrl == null ) 
	    	soundCtrl = new SoundCtrl(); 
	    return soundCtrl; 
	}
	
	private void playSound(File file) {
		AudioInputStream audioInputStream 		= null; 
		BufferedInputStream bufferedInputStream = null; 
		
		try {
//			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(RESSOURCES_SOUND_DOOR_OPEN));
			audioInputStream = AudioSystem.getAudioInputStream( file );
			bufferedInputStream = new BufferedInputStream(audioInputStream);
		    int size = (int) (audioInputStream.getFormat().getFrameSize() * audioInputStream.getFrameLength());
		    byte[] audio = new byte[size];
		    DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat(), size);
		    bufferedInputStream.read(audio, 0, size);
		    final Clip clip = (Clip) AudioSystem.getLine(info);
		    
		    clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						clip.close();
						Thread.currentThread().interrupt();
					}
				}
		    	
		    });
		    
		    clip.open(audioInputStream.getFormat(), audio, 0, size);
		    FloatControl gainControl =
		    	(FloatControl)clip.getControl(Type.MASTER_GAIN);
		    gainControl.setValue(volume);
		    clip.start();
		} catch (UnsupportedAudioFileException ex) {
			throw new GameSoundException("Wrong file type for " + file.getName(), ex);
		} catch (IOException ex) {
			throw new GameSoundException("Problems ocured by reading file for " + file.getName(), ex);
		} catch (LineUnavailableException ex) {
			throw new GameSoundException("Soundfile is allready used for " + file.getName(), ex);
		} finally {
			try {
				bufferedInputStream.close();
				audioInputStream.close();
			} catch (IOException e) {
				// doNotThink
			}
		}
	}

	public void playAttackSound(String fileName) {
		File f = new File( SOUND_FOLDER_ATTACK + fileName ); 
		playSound(f);
	}
	
	public void playFigureDied(String fileName) {
		File f = new File( SOUND_FOLDER_DEATH + fileName );
		playSound(f);
	}
	
//	public void playAttackSound(FigureMapSet figureMapSet) {
//		File f = new File( SOUND_FOLDER_ATTACK + figureMapSet.getAtkSoundName() ); 
//		playSound(f);
//	}
//	
//	public void playFigureDied(FigureMapSet figureMapSet) {
//		File f = new File( SOUND_FOLDER_DEATH + figureMapSet.getDeathSoundName());
//		playSound(f);
//	}
	public void playFigureHit(String fileName) {
		File f = new File( SOUND_FOLDER_HIT + fileName );
		playSound(f);
	}
	
	public void playGameStartSound() {
		File f = new File( SOUND_FOLDER_GAMESTART + "footstep.wav" ); 
		playSound(f);
	}
	
	public void playOpenDoorSound() {
		File f = new File( SOUND_FOLDER_OPENDOOR + "open1.wav" ); 
		playSound(f);
	}
	
	public void playHeroPlayerWin() {
		File f = new File( SOUND_FOLDER_GAMEEND + "heroes.wav"); 
		playSound(f);
	}
	
	public void playMonsterPlayerWin() {
		File f = new File( SOUND_FOLDER_GAMEEND + "evil.wav"); 
		playSound(f);
	}

	/** Returns the value of the attribute {@link #volume}.
	 * @return the volume
	 */
	public static float getVolume() {
		return volume;
	}

	/** Sets the value of the attribute {@link #volume}.
	 * @param volume the volume to set
	 */
	public static void setVolume(float volume) {
		SoundCtrl.volume = volume;
	}
}
