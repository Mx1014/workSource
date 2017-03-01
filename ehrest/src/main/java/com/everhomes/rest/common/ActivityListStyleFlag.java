package com.everhomes.rest.common;

public enum ActivityListStyleFlag {
	ZUOLIN_COMMON((byte)1), OFFICEASY_CUSTOM((byte)2);
	
	private byte code;
	
	private ActivityListStyleFlag(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static ActivityListStyleFlag fromCode(Byte code){
		if (code != null) {
			for (ActivityListStyleFlag flag : ActivityListStyleFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
