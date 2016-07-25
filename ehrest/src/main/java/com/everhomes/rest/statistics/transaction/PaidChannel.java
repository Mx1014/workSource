package com.everhomes.rest.statistics.transaction;

public enum PaidChannel {
	ALIPAY((byte)0), WECHAT((byte)1), PAYMENT((byte)2);
	
	private byte code;
	
	private PaidChannel(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static PaidChannel fromCode(Byte code){
		if(null == code){
			return null;
		}
		PaidChannel[] values = PaidChannel.values();
		for (PaidChannel value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
