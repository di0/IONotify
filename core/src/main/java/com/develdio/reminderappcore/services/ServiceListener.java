package com.develdio.reminderappcore.services;

public interface ServiceListener {
	public boolean isRunning = true;
	public boolean hasStarted = true;
	public boolean hasStopped = true;

	public void start() throws Exception;
	public void stop() throws Exception;
}
