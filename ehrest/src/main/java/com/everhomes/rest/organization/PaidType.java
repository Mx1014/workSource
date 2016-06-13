package com.everhomes.rest.organization;

/**
 * 
 * <ul>
 * <li>1: selfpay, 2: agent</li>
 * </ul>
 *
 */
public enum PaidType {
	
	//1: selfpay, 2: agent
	SELFPAY((byte)1),AGENT((byte)2);
	
	private byte code;
	
	private PaidType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

}
