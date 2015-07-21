package com.everhomes.organization;

public enum OrganizationBillType {
	
	//0: none, 1: bills in eh_organization_bills
	NONE((byte)0),ORGANIZATION_BILLS((byte)1);
	
	private byte code;
	
	private OrganizationBillType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
}
