package com.everhomes.rest.techpark.onlinePay;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0</li>
 *	<li>WAITING_FOR_PAY : 1</li>
 *	<li>PAID : 2</li>
 * </ul>
 *
 */
public enum PayStatus {

	INACTIVE((byte)0),WAITING_FOR_PAY((byte)1),PAID((byte)2);
	
	private byte code;
	
	private PayStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static PayStatus fromStatus(byte code){
		
		for(PayStatus v: PayStatus.values()){
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
