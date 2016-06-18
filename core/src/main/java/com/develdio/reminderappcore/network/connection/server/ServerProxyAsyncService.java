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
package com.develdio.reminderappcore.network.connection.server;

import java.nio.ByteBuffer;
import java.util.List;

import com.develdio.reminderappcore.services.Service;
import com.develdio.reminderappcore.services.async.AsyncService;

public abstract class ServerProxyAsyncService extends AsyncService implements Server, ServerListener {

	// An any server
	private Server server = null;

	// Inject a server default
	public ServerProxyAsyncService( Server server ) {
		this.server = server; 
	}

	@Override
	public abstract void onMessage( String newMessage );

	/**
	 * Starts this Service.
	 */
	@Override
	public void start() {
		// Configure option for this service
		log( "Starting server service" );
		new Thread( this ).start();
		Service.isRunning = true;
	}

	/**
	 * Stop this Service.
	 */
	@Override
	final public void stop() {
		server.stop();
		Service.isRunning = false;
	}

	/**
	 * Verify if this Service is running.
	 */
	@Override
	final public boolean isRunning() {
		return server.isRunning();
	}

	/**
	 * Check if this endpoint was closed.
	 * 
	 * @return boolean True if this endpoint is closed
	 */
	final public boolean isClosed() {
		return server.isClosed();
	}

	/**
	 * Close this Service Endpoint.
	 */
	final public void close() {
		server.close();
	}

	/**
	 * Run method main that this Service Endpoint.
	 */
	final public void run() {
		if ( isRunning() )
		{
			server.start();
		}
	}

	@Override
	public void onNotify( String notice ) {
		log( notice );
	}

	@Override
	public void receive( ByteBuffer o ) {
		server.receive( o );
	}

	@Override
	public void send( ByteBuffer o ) {
		server.send( o );
	}

	@Override
	public void send( String message ) {
		server.send( message );
	}

	@Override
	public List<?> queue() {
		return server.queue();
	}

	@Override
	public int getPort() {
		return server.getPort();
	}

	@Override
	public void listener( ServerListener serverListener ) {
		server.listener( serverListener );
	}
}