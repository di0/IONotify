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

package com.develdio.reminderappcore.network.common;

import java.nio.ByteBuffer;

final public class Payload implements IPayload
{
	// The package data frame
	final private IPackageData packageData;

	// Payload fields
	private byte[] payload;

	private Payload( IPackageData packageData ) {
		this.packageData = packageData;
	}

	public static IPayload payloadFromPackageData( IPackageData packageData ) {
		return new Payload( packageData );
	}

	public void configurePayload( ByteBuffer buffer )
	{
		int payloadLength = packageData.getPayloadLength();

		byte maskingKey[] = null;
		ByteBuffer payload = ByteBuffer.allocate( 4024 );

		// Pacote mascarado
		if ( packageData.isMasked() )
		{
			// Obtem os 32 bits que representa a chave da mascara
			maskingKey = new byte[ 4 ];
			buffer.get( maskingKey, 0, 4 );
			for ( int i = 0; i < payloadLength; i++ )
			{
				// Percorre do inicio ao fim do tamanho do pacote, recuperando os dados
				// e aplicando a chave aos dados encontrados
				payload.put( (byte) ( (byte) buffer.get() ^ (byte) maskingKey[ i % 4 ] ) );
			}
		}
		else
		{
			payload.put( buffer.array(), buffer.position(), payload.limit() );
			buffer.position( buffer.position() + payload.limit() );
		}

		/*
		byte[] payload = new byte[ payloadLength ];
		buffer.get( payload, 0, payloadLength );

		if ( packageData.isMasked() )
		{
			for ( int i = 0; i < payloadLength; i++ )
                        {
                                payload[ i ] ^= maskingKey[ i % 4 ];
                        }
                }

                this.payload = payload;
                */
		this.payload = payload.array();
        }

	/*
	public void configurePayload( ByteBuffer buffer ) {

		int payloadLength = packageData.getPayloadLength();

		byte[] maskingKey = new byte[ 4 ];
		ByteBuffer payload = ByteBuffer.allocate( 4024 );
		if ( packageData.isMasked() )
		{
			maskingKey = new byte[ 4 ];
			buffer.get( maskingKey, 0, 4 );
			for ( int i = 0; i < payloadLength; i++ )
			{
				payload.put( (byte) ( (byte) buffer.get() ^ (byte) maskingKey[ i % 4 ] ) );
			}
		}
		else
		{
			payload.put( buffer.array(), buffer.position(), payload.limit() );
			buffer.position( buffer.position() + payload.limit() );
		}

		this.payload = payload.array();
	}*/

	public byte[] getPayload()
	{
		return this.payload;
	}
}
