package com.develdio.reminderappcore.network.common;

public class FrameVO {
	private boolean isFin;
	private int fin;
	private byte opCode;
	private IPayload payload;
	private int payloadLength;
	private int rsv1, rsv2, rsv3;
	private int mask;
	private boolean isMasked;
	
	public void setIsFin( boolean isFin ) {
		this.isFin = isFin;
	}

	public void setFin( int fin) {
		this.fin = fin;
	}

	public void setOpCode( byte opCode ) {
		this.opCode = opCode;
	}

	public byte getOpCode() {
		return this.opCode;
	}

	public void addPayload( IPayload payload ) {
		this.payload = payload;
	}

	public boolean isFin() {
		return this.isFin;
	}

	public int getFin() {
		return this.fin;
	}

	public void setPayloadLength( int payloadLength ) {
		this.payloadLength = payloadLength;
	}

	public int getPayloadLength() {
		return this.payloadLength;
	}

	public IPayload getPayload() { 
		return this.payload;
	}

	public void addRsv1( int rsv1 ) {
		this.rsv1 = rsv1;
	}

	public void addRsv2( int rsv2 ) {
		this.rsv2 = rsv2;
	}

	public void addRsv3( int rsv3 ) {
		this.rsv3 = rsv3;
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

	public void setMask( int mask ) {
		this.mask = mask;
	}

	public int getMask() {
		return this.mask;
	}

	public void setIsMasked( boolean isMasked ) {
		this.isMasked = isMasked;
	}
	
	public boolean getIsMasked() {
		return this.isMasked;
	}
}
