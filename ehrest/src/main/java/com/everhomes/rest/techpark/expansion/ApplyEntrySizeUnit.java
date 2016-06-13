package com.everhomes.rest.techpark.expansion;

/***
 * SINGLETON(1) 个
 * SQUARE_METERS(2)平方米
 * */
public enum ApplyEntrySizeUnit {
	
	SINGLETON((byte)1), SQUARE_METERS((byte)2);
	
	private byte code;
	
	private ApplyEntrySizeUnit(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ApplyEntrySizeUnit fromType(byte code) {
		for(ApplyEntrySizeUnit v : ApplyEntrySizeUnit.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
