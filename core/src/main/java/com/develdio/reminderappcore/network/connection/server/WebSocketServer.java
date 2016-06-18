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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.develdio.reminderappcore.message.Message;
import com.develdio.reminderappcore.network.common.Frame;
import com.develdio.reminderappcore.network.common.FrameFactory;
import com.develdio.reminderappcore.network.common.HandShake;
import com.develdio.reminderappcore.network.common.IPayload;
import com.develdio.reminderappcore.services.async.AsyncService;

/**
 * A WebSocket Server that encapsule the logic.
 * 
 * @author Diogo Pereira Pinto
 *
 */
final public class WebSocketServer extends AsyncService implements Server {

	// Server side endpoint
	private ServerSocketChannel serverSockSide = null;

	// Client side endpoint
	private SocketChannel clientSockSide = null;

	// Listener this service
	private ServerListener wsListener;

	// Verify if this service is negociated
	private static boolean ALREADY_HANDSHAKE = false;

	// Queue of the Message
	public List< ByteBuffer > queue = new ArrayList< ByteBuffer >();

	// List that treat Message
	final private List< Message > inQueue = new LinkedList< Message >();

	final private List< ByteBuffer > outQueue = new LinkedList< ByteBuffer >();

	// The endpoint server
	private InetSocketAddress hostServer;

	// Default Address
	private String host = "localhost";

	// Default Port
	private int port = 3338;

	private Selector selector;

	public static int i = 1;

	public static String OPCODE_TEXT = "0";

	/**
	 * Initial all configuration to connect a remote foreign Service
	 *
	 * @param host The foreign host to connect to.
	 * @param port The foreign port to connect to.
	 *
  	 * @throws IOException If the connection cannot be established.
	 */
	public WebSocketServer( InetSocketAddress inetSocketAddress ) {
		this.hostServer = inetSocketAddress;
	}

	/**
	 * Create with specified host and port entries.
	 * 
	 * @param host The host name this Service
	 * @param port The port listen this Service
	 * @throws IOException  If the port parameter is outside the range of valid port 
	 * values, or if the hostname parameter is null.
	 * SecurityException - if a security manager is present and permission to resolve the 
	 * host name is denied.
	 */
	public WebSocketServer( String host, int port ) throws IOException {
		this( new InetSocketAddress( host, port ) );
	}

	/**
	 * Create with default configuration.
	 */
	public WebSocketServer() {
		this( new InetSocketAddress( "localhost", 3338 ) );
	}

	public void start() {
		try
		{
			serverSockSide = ServerSocketChannel.open();
			serverSockSide.bind( this.hostServer );
			serverSockSide.configureBlocking( false );

			// Create Selector with accept mode
			selector = Selector.open();
			serverSockSide.register( selector, SelectionKey.OP_ACCEPT );

			// Start this service
			Thread providerThread = new Thread( this );
			providerThread.setName( "WebSocket" );
			providerThread.start();

			// Start service worker
			Thread providerWorkerThread = new Thread( new ProviderSyncServiceWorker() );
			providerWorkerThread.setName( "WebSocketWorker" );
			providerWorkerThread.start();
		}
		catch ( IOException e )
		{
			wsListener.onNotify( e.getMessage() );
		}
	}

	public void stop() {
		
	}

	public void close() {
		try
		{
			if ( endpointIsLive() )
				clientSockSide.close();
			if ( serverSockSide.isOpen() )
				serverSockSide.close();
			if ( selector.isOpen() )
				selector.close();
		}
		catch ( IOException e )
		{
			wsListener.onNotify( e.getMessage() );
		}
	}

	public void run() {

		if ( isRunning() )
		{
			while ( isRunning() )
			{
				try
				{
					selector.select();
					Set< SelectionKey > keys = selector.selectedKeys();
					Iterator< SelectionKey > iter = keys.iterator();
	
					while ( iter.hasNext() )
					{
						SelectionKey key = iter.next();
						iter.remove();

						// Ignore invalid key
						if ( !key.isValid() )
							wsListener.onNotify( "Invalid key: " + key.toString() );
	
						// Try to accept connection from endpoint
						// altought handshake negociation
						if ( key.isAcceptable() )
						{
							connectWithEndPoint( key );
						}
	
						// Read from endpoint
						if ( key.isReadable() )
						{
							if ( endpointIsLive() )
							{
								wsListener.onNotify( "Service is reading " );
								clientSockSide = (SocketChannel) key.channel();
								ByteBuffer buffer = ByteBuffer.allocate( 1024 );
								clientSockSide.read( buffer );
								receive( buffer );
								clientSockSide.register( selector, SelectionKey.OP_WRITE );
							}
							else
							{
								wsListener.onNotify( "Client isn't logged" );
							}
						}

						if ( key.isWritable() )
						{
							if ( endpointIsLive() )
							{
								ByteBuffer buffer = ByteBuffer.allocate( 1024 );
								clientSockSide = (SocketChannel) key.channel();
								send( buffer, true );

								if ( ! queue().isEmpty() )
								{
									clientSockSide.write( (ByteBuffer) queue().get( 0 ) );
									queue().remove( 0 );
								}

								if ( ! outQueue.isEmpty() )
								{
									clientSockSide.write( (ByteBuffer) outQueue.get( 0 ) );
									outQueue.remove( 0 );
								}

								clientSockSide.register( selector, SelectionKey.OP_READ );
								buffer.clear();
							}
							else
							{
								wsListener.onNotify( "Client isn't logged" );
							}
						}
					}
				}
				catch ( IOException e )
				{
					try
					{
						wsListener.onNotify( "IO Exception Error: " + e.getMessage() );
						clientSockSide.close();
					}
					catch ( IOException e1 )
					{
						wsListener.onNotify( e1.getMessage() );
					}
				}
				finally
				{
					try
					{
						Thread.sleep( 600 );
						// close();
						// start();
					}
					catch ( InterruptedException ie )
					{
						wsListener.onNotify( "Interrupted Exception: " + ie.getMessage() );
						continue;
					}
				}
			}
		}
		else
		{
			wsListener.onNotify( "Service is not running" );
		}
	}

	// Add a listener
	public void listener( ServerListener wsListener ) {
		this.wsListener = wsListener;
	}

	/**
	 * Sends message to endpoint
	 * @param buffer Message that should sended
	 */
	public void send( ByteBuffer buffer ) {
		/*
		if ( WebSocketServer.ALREADY_HANDSHAKE == false )
		{
			buffer.put( HandShake.prepareResponse().getBytes() );
			buffer.flip();
			queue.add( buffer );
			WebSocketServer.ALREADY_HANDSHAKE = true;
		}
		else
		{*/
			buffer.flip();
			byte[] o = new byte[ buffer.remaining() ];
			int i = 0;
			while ( buffer.hasRemaining() )
			{
				o[ i ] = buffer.get();
				i++;
			}
			String charbuffer =
					new String ( o, Charset.forName( "UTF-8" ) );
			wsListener.onNotify( "Sending message " + charbuffer );
			queue.add( buffer );

			/*
			ByteBuffer newBuffer = ByteBuffer.allocate( 1000 );
			byte[] b = new byte[ buffer.remaining() ];
			while ( buffer.hasRemaining() )
			{
				newBuffer.put ( (byte) ( buffer.get() | 8 ) );
			}
			outQueue.add( buffer );*/
		//}
	}

	public void send( String message ) {

		log( "Sending message: " + message );
		ByteBuffer buffer = ByteBuffer.allocate( ( message.length() + 4024 ) );

		buffer.put( (byte) 0x81 );

		if ( ( message.length() > 0 ) && ( message.length() <= 125 ) )
		{
			buffer.put( (byte) message.length() );
			buffer.put( message.getBytes() );
		}
		else
		{
			if ( ( message.length() >= 0x7e ) && ( message.length() <= 0xffff ) )
			{
				buffer.put( (byte) 0x7e );

				buffer.put( (byte) ( message.length() >> 0x8 ) );
				buffer.put( (byte) ( message.length() & 0xff ) );

				buffer.put( message.getBytes() );
			}
			else if ( message.length() >= 0x10000 )
			{
				buffer.put( (byte) 0x7f );

				buffer.put( (byte) 0x0 );
				buffer.put( (byte) 0x0 );
				buffer.put( (byte) 0x0 );
				buffer.put( (byte) 0x0 );
				buffer.put( (byte) 0x0 );
				buffer.put( (byte) 0x0 );
				buffer.put( (byte) 0x0 );
				buffer.put( (byte) message.length() );

				buffer.put( message.getBytes() );
			}
		}

		buffer.flip();
		queue.add( buffer );
	}

	public void send( ByteBuffer buffer, boolean yesNo ) {

		if ( WebSocketServer.ALREADY_HANDSHAKE == false )
		{
			buffer.put( HandShake.prepareResponse().getBytes() );
			buffer.flip();
			queue.add( buffer );
			WebSocketServer.ALREADY_HANDSHAKE = true;
		}
	}

	/**
	 * Receives message from endpoint.
	 * @param buffer The stream with data
	 */
	public void receive( ByteBuffer buffer ) {

		buffer.flip();

		if ( WebSocketServer.ALREADY_HANDSHAKE == false )
		{
			try
			{
				wsListener.onNotify( "HandShake has been started" );
				HandShake.translateHeaderHttp( buffer, "client" );
				buffer.flip();
				Charset charset = Charset.forName( "UTF8" );
				CharsetDecoder decoder = charset.newDecoder();
				CharBuffer charbuffer = decoder.decode( buffer );
				wsListener.onNotify( "HandShake successfully accepted" );
				wsListener.onNotify( "\n" + charbuffer.toString() );
			}
			catch ( CharacterCodingException e )
			{
				wsListener.onNotify( e.getMessage() );
			}
		}
		else
		{
			synchronized ( this )
			{
				if ( buffer.hasRemaining() )
				{
					Frame frame = FrameFactory.createFrame( buffer );
					if ( frame.isFin() )
					{
						wsListener.onNotify( "End of message" );
					}

					// Treat opcode fragments
					treatOpcode( frame.getOpCode() );

					// Get Payload
					IPayload payload = frame.getPayload();
					wsListener.onNotify( "Size payload: " + frame.getPayloadLength() );

					if ( WebSocketServer.OPCODE_TEXT.equals( "1" ) )
					{
						// Sends message 
						String charbuffer =
								new String ( payload.getPayload() , Charset.forName( "UTF-8" ) );
						wsListener.onMessage( charbuffer );
					}
				}
			}
		}
	}

	@Override
	public int getPort() {
		return 1;
	}

	final public boolean isClosed() {
		return ( ! serverSockSide.isOpen() );
	}

	public boolean hasHandshakePendency() {
		return WebSocketServer.ALREADY_HANDSHAKE;
	}

	@Override
	public List<ByteBuffer> queue() {
		return this.queue;
	}

	@Override
	public boolean isRunning() {
		return true;
		/*
		return ( ! ( Thread.currentThread().isInterrupted()
				? true : false ) );
		*/ 
	}

	private void connectWithEndPoint( SelectionKey key ) throws IOException {
		ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
		clientSockSide = serverSocket.accept();
		if ( clientSockSide.isConnected() )
		{
			wsListener.onNotify( "Client has been connected" );
		}

		wsListener.onNotify( "Service has been connected at: " + clientSockSide
				.getRemoteAddress() );
		clientSockSide.configureBlocking( false );
		clientSockSide.register( selector, SelectionKey.OP_READ );
	}

	private boolean endpointIsLive() {
		return ( clientSockSide.isConnected() && clientSockSide.isOpen() );
	}

	private void treatOpcode( byte opCode ) {
		if ( opCode == 0x8 )
		{
			wsListener.onNotify( "Client was disconnected" );
		}

		if ( opCode == 0x9 )
		{
			wsListener.onNotify( "Client is pinging" );
		}

		if ( opCode == 0x0 )
		{
			wsListener.onNotify( "Continuation frame" );
		}

		if ( opCode == 0x2 )
		{
			wsListener.onNotify( "Binary frame" );
		}

		if ( opCode == 0x1 )
		{
			WebSocketServer.OPCODE_TEXT = "1";
			wsListener.onNotify( "Text frame" );
		}
	}

	/**
	 * Class that work for Provider
	 */
	public class ProviderSyncServiceWorker extends Thread {
		@Override
		public void run()
		{
			while ( isRunning() )
			{
				if ( ! inQueue.isEmpty() )
				{
					Message message = inQueue.get( 0 );
					wsListener.onNotify( "New message arrivied: " +  message.getContent() );
					inQueue.remove( 0 );
					yield();
				}

				else
				{
					try
					{
						Thread.sleep( 3000 );
					}
					catch ( InterruptedException e )
					{
						wsListener.onNotify( e.getMessage() );
					}
				}
			}
		}
	}
}
