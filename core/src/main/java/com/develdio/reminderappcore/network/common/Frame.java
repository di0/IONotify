package com.develdio.reminderappcore.network.common;

public interface Frame {

	public byte getOpCode();

	public boolean isFin();

	public int getFin();

	public int getPayloadLength();

	public IPayload getPayload();
}
