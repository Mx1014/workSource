package com.everhomes.rest.techpark.expansion;

/**
 * APPLY(1):申请
 * EXPANSION(2): 扩租
 * RENEW(3):续租
 * */
public enum ApplyEntryApplyType {
	
	APPLY((byte)1), EXPANSION((byte)2), RENEW((byte)3);
	
	private byte code;
	
	private ApplyEntryApplyType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ApplyEntryApplyType fromType(byte code) {
		for(ApplyEntryApplyType v : ApplyEntryApplyType.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
