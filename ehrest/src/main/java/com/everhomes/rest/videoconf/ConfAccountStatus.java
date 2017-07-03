package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * INACTIVE: 0 不活动?
 * ACTIVE: 1 活动?.
 * LOCKED: 2 锁定?
 *
 */
public enum ConfAccountStatus {

	INACTIVE((byte)0), ACTIVE((byte)1),LOCKED((byte)2);
	
	private byte code;
	
	private ConfAccountStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ConfAccountStatus fromStatus(byte code) {
		for(ConfAccountStatus v : ConfAccountStatus.values()) {
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
