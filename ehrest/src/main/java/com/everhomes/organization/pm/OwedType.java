package com.everhomes.organization.pm;

public enum OwedType {
	
	OWED((byte)1),NO_OWED((byte)2);
	
	private byte code;
	
	private OwedType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	

}
