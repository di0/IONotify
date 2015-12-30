package com.develdio.reminderappcore.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.develdio.reminderappcore.logs.Log;

public class Log {
	private static Logger log;

	private Log(Class<?> clazz) {
		log = LogManager.getLogger(clazz);
	}

	public static Log getLogInstance(Class<?> clazz) {
		return new Log(clazz);
	}

	public void logDebug(String notice) {
		log.debug(notice);
	}
}
