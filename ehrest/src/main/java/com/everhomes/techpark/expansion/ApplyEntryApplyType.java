package com.everhomes.techpark.expansion;


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
