package com.everhomes.rest.organization.pm;

/**
 * 
 * 不欠费(1),欠费(2)
 *
 */
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
