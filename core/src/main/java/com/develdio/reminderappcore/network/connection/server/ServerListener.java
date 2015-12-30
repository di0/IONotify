package com.develdio.reminderappcore.network.connection.server;

public interface ServerListener {
	public void onMessage ( String message );
	public void onNotify( String notice );
}
