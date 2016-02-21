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

final public class FrameFactory {

	/**
	 * Create a Frame from Package Data with bits of the
	 * buffer stream.
	 * 
	 * @param buffer The buffer stream
	 * @return The Frame factory
	 */
	public static Frame createFrame( ByteBuffer buffer ) {

		// Frame values representation
		FrameVO frameVO = new FrameVO();

		// octet with FIN, RSV and OPCODE
		IPackageData firstPart = new PackageData
				.PackageDataBuilder( buffer ).withFinRsvAndOpcode().createPackageData();

		frameVO.setIsFin( firstPart.isFin() );
		frameVO.setFin( firstPart.getFin() );
		frameVO.addRsv1( firstPart.getRsv1() );
		frameVO.addRsv2( firstPart.getRsv2() );
		frameVO.addRsv3( firstPart.getRsv3() );
		frameVO.setOpCode( firstPart.getOpCode() );

		// octet with Mask and Payload Length
		IPackageData secondPart = new PackageData.PackageDataBuilder( buffer )
				.withMaskAndPayloadLength().createPackageData();

		frameVO.setMask( secondPart.getMask() );
		frameVO.setIsMasked( secondPart.isMasked() );
		frameVO.setPayloadLength( secondPart.getPayloadLength() );

		// The Payload Data
		IPayload payload = Payload.payloadFromPackageData( secondPart );
		payload.configurePayload( buffer );
		frameVO.addPayload( payload );

		return new FrameFactory.FrameOperation( frameVO );
	}

	/**
	 * Class that represent the real Frame.
	 *
	 */
	final private static class FrameOperation implements Frame {

		private FrameVO frameVO = null ; 

		public FrameOperation( FrameVO frameVO ) {
			this.frameVO = frameVO;
		}

		public boolean isFin() {
			return frameVO.isFin();
		}

		public int getFin() {
			return frameVO.getFin();
		}

		public byte getOpCode() {
			return frameVO.getOpCode();
		}

		public int getPayloadLength() {
			return frameVO.getPayloadLength();
		}

		public IPayload getPayload() {
			return frameVO.getPayload();
		}

		public int getRsv1() {
			return frameVO.getRsv1();
		}

		public int getRsv2() {
			return frameVO.getRsv2();
		}

		public int getRsv3() {
			return frameVO.getRsv3();
		}
	}
}