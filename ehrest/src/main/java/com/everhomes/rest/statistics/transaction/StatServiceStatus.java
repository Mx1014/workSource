package com.everhomes.rest.statistics.transaction;

public enum StatServiceStatus {
	INACTIVE((byte)0), ACTIVE((byte)1);
	
	private byte code;
	
	private StatServiceStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static StatServiceStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		StatServiceStatus[] values = StatServiceStatus.values();
		for (StatServiceStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
