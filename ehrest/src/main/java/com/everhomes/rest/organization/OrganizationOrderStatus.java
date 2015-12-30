package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>INACTIVE : 0</li>
 *	<li>WAITING_FOR_PAY : 1</li>
 *	<li>PAID : 2</li>
 *</ul>
 *
 */
public enum OrganizationOrderStatus {
	INACTIVE((byte)0),WAITING_FOR_PAY((byte)1),PAID((byte)2);
	
	private byte code;
	private OrganizationOrderStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static OrganizationOrderStatus fromCode(byte code){
		for(OrganizationOrderStatus v : OrganizationOrderStatus.values()){
			if(v.getCode() == code)
				return v;
		}
		return null;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
