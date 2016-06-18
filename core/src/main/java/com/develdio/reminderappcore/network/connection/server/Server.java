package com.develdio.reminderappcore.network.connection.server;

import java.nio.ByteBuffer;
import java.util.List;

public interface Server {
	public void start();
	public void stop();
	public boolean isRunning();
	public void close();
	public boolean isClosed();
	public void receive( ByteBuffer o );
	public void send( ByteBuffer o );
	public void send( String message );
	public List<?> queue();
	public int getPort();
	public void listener( ServerListener serverListener );
}
