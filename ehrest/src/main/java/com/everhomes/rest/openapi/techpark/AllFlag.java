package com.everhomes.rest.openapi.techpark;

public enum AllFlag {
	ALL((byte)1), NOT_ALL((byte)0);
	
	private Byte code;
	
	private AllFlag(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	public static AllFlag fromCode(Byte code) {
		if (code != null) {
			for (AllFlag allFlag : AllFlag.values()) {
				if (allFlag.getCode() == code.byteValue()) {
					return allFlag;
				}
			}
		}
		return null;
	}
}
