package com.develdio.reminderappcore.network.connection.server.spi;

import com.develdio.reminderappcore.network.connection.server.Server;
import com.develdio.reminderappcore.network.connection.server.WebSocketServer;

public class ServerFactory {
	public static Server createServer() {
		return new WebSocketServer();
	}
}
