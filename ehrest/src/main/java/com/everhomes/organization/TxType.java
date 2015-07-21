package com.everhomes.organization;

public enum TxType {

	//1: online, 2: offline
	ONLINE((byte)1),OFFLINE((byte)2);
	
	private byte code;
	
	private TxType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	
}
