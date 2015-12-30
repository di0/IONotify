package com.develdio.reminderappcore.network.common;

public interface IPackageData {
	public boolean isFin();

	public int getFin();

	public boolean isValidRsv();

	public byte getOpCode();

	public int getMask();

	public boolean isMasked();

	public IPayload getPayload();

	public int getPayloadLength();

	public byte getRestPayloadLength();

	public int getRsv1();

	public int getRsv2();

	public int getRsv3();
}
