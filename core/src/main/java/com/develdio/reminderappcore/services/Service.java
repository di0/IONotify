package com.develdio.reminderappcore.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.develdio.reminderappcore.logs.Log;
import com.develdio.reminderappcore.services.async.AsyncService;

public abstract class Service {
	protected static boolean hasStarted = false;
	protected static boolean hasStopped = false;
	protected static boolean isRunning = false;

	private static String identifier = "";

	private static Logger log;

	public abstract void start();
	public abstract void stop();
	public abstract boolean isRunning();

	public static enum Action {
		S_START, S_STOP;
	}

	public Service() {
		Class<?> c = this.getClass();

		if (!"".equals(identifier))
		{
			if (!identifier.equals(c.getName()))
			{
				// Reset flag
				Service.hasStarted = false;
				log = LogManager.getLogger(c.getName());
			}
		}
		else
		{
			identifier = c.getName();
			log = LogManager.getLogger( c.getName() );
		}
	}

	public void exec(Action action) throws Exception {
		if ( action == Action.S_START )
			startService();
		if ( action == Action.S_STOP )
			stopService();
	}

	/**
	 * Starts this service.
	 */
	private void startService() throws Exception {
		if ( Service.hasStarted )
		{
			String mError = "Service is already running";
			log( mError );
			throw new Exception( mError );
		}

		start();
	}

	/**
	 * Closes communication of this Service
	 */
	private void stopService() throws Exception {
		if ( Service.hasStopped )
		{
			String mError = "Service is not running";
			log.warn( mError );
			throw new Exception( mError );
		}

		stop();
	}

	/**
	 * Interrupts communication of this Service
	 */
	protected void pauseService() {
		// TODO store state
	}

	/**
	 * Restarts communication of this Service 
	 */
	protected void restart() throws Exception {
		start();
		stop();
	}

	final public void log( String notice ) {
		Log.getLogInstance( AsyncService.class ).logDebug( notice );
	}
}
