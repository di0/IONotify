package com.develdio.reminderappcore.network.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.develdio.reminderappcore.util.Base64;

public class HandShake {

	public static Map< String, String > clientHeader = new HashMap< String, String >();
	public static Map< String, String > serverHeader = new HashMap< String, String >();

	public static Map< String, String > translateHeaderHttp( ByteBuffer buf, String who ) {

		String line = readStringLine( buf );
		line = readStringLine( buf );

		while ( ( line != null ) && ( line.length() > 0 ) )
		{
			String[] pair = line.split( ":", 2 );
			if ( pair.length != 2 )
			{
				// TODO lancar excecao.
				System.out.println( "Error" );
				System.exit( 1 );
			}
			if ( "client".equalsIgnoreCase( who ) )
				clientHeader.put( pair[ 0 ], pair[ 1 ].replaceFirst( "^ +", "" ) );
			else
				serverHeader.put( pair[ 0 ], pair[ 1 ].replaceFirst( "^ +", "" ) );
			line = readStringLine( buf );
		}

		if ( "client".equalsIgnoreCase( who ) )
			return clientHeader;
		return serverHeader;
	}

	public static Map< String, String > getHeaderHttpFromClient() {
		return HandShake.clientHeader;
	}

	private static String generateFinalKey( String in ) {
		String seckey = in.trim();
		String acc = seckey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		MessageDigest sh1;
		try
		{
			sh1 = MessageDigest.getInstance( "SHA1" );
		}
		catch ( NoSuchAlgorithmException e )
		{
			throw new RuntimeException( e );
		}
		return Base64.encodeBytes( sh1.digest( acc.getBytes() ) );
	}

	private static String readStringLine( ByteBuffer buf ) {
		try
		{
			ByteBuffer newBuff = readLine(buf);
			return new String( newBuff.array(), 0, newBuff.limit(), "ASCII" );
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String prepareResponse() {
		StringBuilder response = new StringBuilder();
		response.append( "HTTP/1.1 101 Switching Protocols\r\n" );
		response.append( "Upgrade: websocket\r\n" );
		response.append( "Connection: Upgrade\r\n" );
		response.append( "Sec-WebSocket-Accept: " + generateFinalKey
				( clientHeader.get("Sec-WebSocket-Key")) + "\r\n" );
		response.append( "\r\n" );

		return response.toString();
	}

	private static ByteBuffer readLine( ByteBuffer buf ) {

		ByteBuffer sbuf = ByteBuffer.allocate( buf.remaining() );
		byte prev = '0';
		byte cur = '0';
		while ( buf.hasRemaining() )
		{
			prev = cur;
			cur = buf.get();
			sbuf.put( cur );
			if ( prev == (byte) '\r' && cur == (byte) '\n' )
			{
				sbuf.limit( sbuf.position() - 2 );
				sbuf.position( 0 );
				return sbuf;
			}
		}
		// ensure that there wont be any bytes skipped
		buf.position( buf.position() - sbuf.position() );
		return null;
	}
}
