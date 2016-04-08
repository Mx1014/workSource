package com.everhomes.rest.repeat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>INACTIVE : 0</li>
 *	<li>WAITING : 1</li>
 *	<li>ACTIVE : 2</li>
 * </ul>
 *
 */
public enum RepeatSettingStatus {

	INACTIVE((byte)0), WAITING((byte)1), ACTIVE((byte)2);
	
	private byte code;
	
	private RepeatSettingStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static RepeatSettingStatus fromStatus(byte code) {
		for(RepeatSettingStatus v : RepeatSettingStatus.values()) {
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
