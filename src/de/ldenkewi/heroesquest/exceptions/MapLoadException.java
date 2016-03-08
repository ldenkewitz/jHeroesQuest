package de.ldenkewi.heroesquest.exceptions;

import org.apache.log4j.Logger;

public class MapLoadException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(MapLoadException.class.getName());
	
	public MapLoadException() {
		super();
		logger.debug("MapLoadException ocured");
	}
	
	public MapLoadException(String exeptionText) {
		super(exeptionText);
		logger.debug(exeptionText);
	}
	
	public MapLoadException(String exeptionText, Throwable th) {
		super(exeptionText, th);
		logger.debug(exeptionText, th);
	}
}
