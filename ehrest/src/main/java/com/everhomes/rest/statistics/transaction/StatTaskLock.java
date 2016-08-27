package com.everhomes.rest.statistics.transaction;

public enum StatTaskLock {
	UNLOCK((byte)0), LOCK((byte)1);
	
	private byte code;
	
	private StatTaskLock(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static StatTaskLock fromCode(Byte code){
		if(null == code){
			return null;
		}
		StatTaskLock[] values = StatTaskLock.values();
		for (StatTaskLock value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
