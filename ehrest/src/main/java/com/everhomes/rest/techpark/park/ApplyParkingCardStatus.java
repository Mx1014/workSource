package com.everhomes.rest.techpark.park;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0</li>
 *	<li>WAITING : 1</li>
 *	<li>NOTIFIED : 2</li>
 *  <li>FETCHED : 3</li>
 * </ul>
 *
 */
public enum ApplyParkingCardStatus {

	INACTIVE((byte)0), WAITING((byte)1), NOTIFIED((byte)2), FETCHED((byte)3);
	
	private byte code;
	
	private ApplyParkingCardStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ApplyParkingCardStatus fromStatus(byte code) {
		for(ApplyParkingCardStatus v : ApplyParkingCardStatus.values()) {
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
