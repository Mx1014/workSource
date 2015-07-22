package com.everhomes.organization;

public enum BillingAccountType {
	USER((byte)101),FAMILY((byte)102),ORGANIZATION((byte)103);
	
	private byte code;
	
	private BillingAccountType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	

}
