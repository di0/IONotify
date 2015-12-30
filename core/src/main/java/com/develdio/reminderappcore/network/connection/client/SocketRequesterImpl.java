/*
 *  Copyright 2014 Diogo Pereira Pinto <dio@lognull.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.develdio.reminderappcore.network.connection.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

public class SocketRequesterImpl implements SocketRequester {
	// Client side
	private Socket client_c = null;
	
	// Default Address
	private String host;

	// Default Port
	private int port;

	// Stream input
	private PrintStream in;

	// Stream output
	private PrintStream out;

	/**
	 * Initial all configuration to connect a remote foreign Service
	 *
	 * @param host the foreign host to connect to.
	 * @param port the foreign port to connect to.
	 * @throws IOException if the connection cannot be established.
	*/
	public SocketRequesterImpl(String host, int port) {
			this.host = host;
			this.port = port;
	}

	/**
	 * Initial default configuration to connect a remote foreign Service
	 *
	 * @param host the foreign host to connect to.
	 * @param port the foreign port to connect to.
	 * @throws IOException if the connection cannot be established.
	 */
	public static SocketRequester fromSocketRequesterDefault() {
		return new SocketRequesterImpl("localhost", 9999);
	}

	/**
	 * Connect this host at endpoint server
	 */
	public void connect() {
		try {
			client_c = SocketFactory.getDefault().createSocket();
			client_c.connect(new InetSocketAddress(this.host, this.port));
			out = new PrintStream(client_c.getOutputStream());
			out.flush();
			in = new PrintStream(client_c.getOutputStream());
		} catch (IOException e) {}
	}

	public boolean isConnected() {
		return  client_c.isConnected();
	}

	public boolean isClosed() {
		return client_c.isClosed();
	}

	public void close() {
		try {
			client_c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Object message) {
		out.println(message);
		out.flush();
	}

	public String read() {
		String flow = new String();
		return flow;
	}
}
