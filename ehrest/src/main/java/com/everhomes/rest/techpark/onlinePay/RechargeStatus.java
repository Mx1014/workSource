package com.everhomes.rest.techpark.onlinePay;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>INACTIVE : 0</li>
 *	<li>HANDING : 1</li>
 *	<li>UPDATING : 2</li>
 *  <li>SUCCESS : 3</li>
 * </ul>
 *
 */
public enum RechargeStatus {
	INACTIVE((byte)0), HANDING((byte)1), UPDATING((byte)2), SUCCESS((byte)3);
	
	private byte code;
	
	private RechargeStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static RechargeStatus fromStatus(byte code){
		
		for(RechargeStatus v: RechargeStatus.values()){
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
