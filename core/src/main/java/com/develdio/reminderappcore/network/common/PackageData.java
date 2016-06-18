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
import java.math.BigInteger;

/**
 * Class read-only that can represent the Package Data of multiples
 * way.
 * 
 */
final public class PackageData implements IPackageData {

	private boolean isFin = false;
	private int fin = 0;
	private byte opCode = 0;
	private IPayload payload = null;
	private int payloadLength = 0;
	private byte restPayloadLength = 0;
	private int rsv1, rsv2, rsv3 = 0;
	private int mask = 0;
	private boolean isMasked;
	private boolean isValidRsv;

	public PackageData( PackageDataBuilder packageDataBuilder ) {
		this.isFin = packageDataBuilder.isFin();
		this.opCode = packageDataBuilder.getOpCode();
		this.payloadLength = packageDataBuilder.getPayloadLength();
		this.fin = packageDataBuilder.getFin();
		this.rsv1 = packageDataBuilder.getRsv1();
		this.rsv2 = packageDataBuilder.getRsv2();
		this.rsv3 = packageDataBuilder.getRsv3();
		this.mask = packageDataBuilder.getMask();
		this.isMasked = packageDataBuilder.isMasked();
		this.isValidRsv = packageDataBuilder.isValidRsv();
	}

	public boolean isFin() {
		return this.isFin;
	}

	public int getFin() {
		return this.fin;
	}

	public boolean isValidRsv() {
		return isValidRsv;
	}

	public byte getOpCode() {
		return this.opCode;
	}

	public int getMask() {
		return this.mask;
	}

	@Override
	public boolean isMasked() {
		return this.isMasked;
	}

	public IPayload getPayload() {
		return payload;
	}

	public int getPayloadLength() {
		return this.payloadLength;
	}

	public byte getRestPayloadLength() {
		return this.restPayloadLength;
	}

	public int getRsv1() {
		return this.rsv1;
	}

	public int getRsv2() {
		return this.rsv2;
	}

	public int getRsv3() {
		return this.rsv3;
	}

	public static class PackageDataBuilder {

		final private ByteBuffer buffer;

		private int fin = 0;
		private boolean isFin = false;
		private byte opCode = 0x0;
		private int payloadLength = 0x0;
		private int rsv1, rsv2, rsv3 = 0;
		private boolean isValidRsv = false;
		private int mask = 0;
		private boolean isMasked = false;

		public PackageDataBuilder( ByteBuffer buffer ) {
			this.buffer = buffer;
		}

		public PackageDataBuilder withFinRsvAndOpcode() {
			byte b = buffer.get();

			// The fin field
			this.fin = b >> 0x8;
			this.isFin = ( ( b >> 0x8 ) != 0 );

			// The RSV field
			this.rsv1 = ( b & 0x40 );
			this.rsv2 = ( b & 0x20 );
			this.rsv3 = ( b & 0x10 );

			this.isValidRsv = ( rsv1 != 0 && rsv2 != 0 && rsv3 != 0 );
			this.opCode = ( (byte) ( b & 0xF ) );

			return this;
		}

		public PackageDataBuilder withMaskAndPayloadLength() {

			byte b = this.buffer.get();

			// The masked field
			this.mask = ( b & -0x80 );
			this.isMasked =  ( ( b & -0x80 ) != 0 );

			// The payload length field
			this.payloadLength = ( (byte) ( b & ~ (byte) 0x80 ) );

			int byteExtra = 0x0;
			if ( this.payloadLength == 0x7E )
			{
				byteExtra = 0x2;
			}
			if ( this.payloadLength == 0x7F )
			{
				byteExtra = 0x8;
			}

			// Decode Payload Length
			int i = 1;
			byte[] sizebytes = new byte[ 3 ];
			boolean f = false;
			while ( --byteExtra > 0 )
			{
				sizebytes[ i ] = buffer.get();
				sizebytes[ i + 1 ] = buffer.get();
				i++;
				f = true;
			}
			if ( f ) {
				payloadLength = new BigInteger( sizebytes ).intValue();
			}

			return this;
		}

		public int getFin() {
			return this.fin;
		}

		public boolean isFin() {
			return this.isFin;
		}

		public byte getOpCode() {
			return this.opCode;
		}

		public int getPayloadLength() {
			return this.payloadLength;
		}

		public int getMask() {
			return this.mask;
		}

		public boolean isMasked() {
			return this.isMasked;
		}

		public boolean isValidRsv() {
			return this.isValidRsv;
		}

		public int getRsv1() {
			return this.rsv1;
		}

		public int getRsv2() {
			return this.rsv2;
		}

		public int getRsv3() {
			return this.rsv3;
		}

		public PackageData createPackageData() {
			return new PackageData( this );
		}
	}
}
