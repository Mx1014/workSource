package com.everhomes.organization.pm;

public enum OwedType {
	
	NO_OWED((byte)1),OWED((byte)2);
	
	private byte code;
	
	private OwedType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	

}
