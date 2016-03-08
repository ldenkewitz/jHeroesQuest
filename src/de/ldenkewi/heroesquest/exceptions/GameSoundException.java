package de.ldenkewi.heroesquest.exceptions;

import org.apache.log4j.Logger;

public class GameSoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(GameSoundException.class.getName());
	
	public GameSoundException() {
		super();
		logger.debug("GameSoundException ocured");
	}
	
	public GameSoundException(String exeptionText) {
		super(exeptionText);
		logger.debug(exeptionText);
	}
	
	public GameSoundException(String exeptionText, Throwable th) {
		super(exeptionText, th);
		logger.debug(exeptionText, th);
	}
}
