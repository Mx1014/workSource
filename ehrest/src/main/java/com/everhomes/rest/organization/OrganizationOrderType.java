package com.everhomes.rest.organization;
/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>ORGANIZATION_ORDERS : 1</li>
 *</ul>
 *
 */
public enum OrganizationOrderType {
	
	//0: none, 1: orders in eh_organization_orders
	NONE((byte)0),ORGANIZATION_ORDERS((byte)1);
	
	private byte code;
	
	private OrganizationOrderType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
}
