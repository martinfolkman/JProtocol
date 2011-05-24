package org.jprotocol.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LazyLogger {
	private final Logger logger;

	private LazyLogger(Logger logger) {
		this.logger = logger;
	}

	public static LazyLogger getLogger(@SuppressWarnings("rawtypes") Class clazz) {
		return new LazyLogger(Logger.getLogger(clazz.getName()));
	}
	
	public void warning(Object...objs) {
		if (logger.isLoggable(Level.WARNING)) {
			logger.warning(message(objs));
		}
	}
	public void finest(Object...objs) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(message(objs));
		}
	}
	public void fine(Object...objs) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(message(objs));
		}
	}
	public void info(Object...objs) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info(message(objs));
		}
	}

	private String message(Object[] objs) {
		StringBuffer buf = new StringBuffer();
		for (Object o: objs) {
			buf.append(o);
		}
		return buf.toString();
	}
}
