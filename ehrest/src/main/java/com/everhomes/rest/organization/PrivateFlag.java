package com.everhomes.rest.organization;

public enum PrivateFlag {
	
	//'0: public, 1: private'
	PUBLIC((byte)0),PRIVATE((byte)1);
	
	private byte code;
	
	private PrivateFlag(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static PrivateFlag fromCode(Byte code) {
		if (code != null) {
			for (PrivateFlag privateFlag : PrivateFlag.values()) {
				if (code.byteValue() == privateFlag.getCode()) {
					return privateFlag;
				}
			}
		}
		return null;
	}

}
