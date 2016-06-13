package com.everhomes.rest.organization;

public enum AccountType {
	
	//'0: none, 1: user, 2: family, 3: organization'
	NONE((byte)0),USER((byte)1),FAMILY((byte)2),ORGANIZATION((byte)3);
	
	private byte code;
	
	private AccountType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	

}
