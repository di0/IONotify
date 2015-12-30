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

final public class Payload implements IPayload {

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

	public void configurePayload( ByteBuffer buffer ) {
		int payloadLength = packageData.getPayloadLength();

		if ( packageData.isMasked() )
		{
			byte[] maskingKey = new byte[ 4 ];
			buffer.get( maskingKey, 0, 4 );

			byte[] payload = new byte[ payloadLength ];
			buffer.get( payload, 0, payloadLength );

			for ( int i = 0; i < payloadLength; i++ )
			{
				payload[ i ] ^= maskingKey[ i % 4 ];
			}

			this.payload = payload;
		}
	}

	public byte[] getPayload() {
		return this.payload;
	}
}
