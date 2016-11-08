package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0 无效</li>
 *	<li>ACTIVE : 1 正常</li>
 * </ul>
 *
 */
public enum Status {
	INACTIVE((byte)0), ACTIVE((byte)1);
	
	private byte code;
	
	private Status(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static Status fromStatus(byte code) {
		for(Status v : Status.values()) {
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
