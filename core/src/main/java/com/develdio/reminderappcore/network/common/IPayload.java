package com.develdio.reminderappcore.network.common;

import java.nio.ByteBuffer;

public interface IPayload {
	public void configurePayload( ByteBuffer buffer );
	public byte[] getPayload();
}
