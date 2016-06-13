package com.everhomes.rest.organization;

public enum PmManagementIsAll {
	
	//'0: public, 1: private'
	ALL((byte)0),SECTION((byte)1);
	
	private byte code;
	
	private PmManagementIsAll(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	

}
