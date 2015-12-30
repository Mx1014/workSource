package com.everhomes.rest.techpark.expansion;

/**
 * PROCESSING(1):处理中
 * RESIDED_IN(2): 已入住
 * INVALID(3):已作废
 * */
public enum ApplyEntryStatus {
	
	PROCESSING((byte)1), RESIDED_IN((byte)2), INVALID((byte)3);
	
	private byte code;
	
	private ApplyEntryStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ApplyEntryStatus fromType(byte code) {
		for(ApplyEntryStatus v : ApplyEntryStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
