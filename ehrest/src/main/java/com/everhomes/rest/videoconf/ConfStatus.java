package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * BEFORE_CONFERENCE: 0
 * IN_CONFERENCE: 1
 *
 */
public enum ConfStatus {

	BEFORE_CONFERENCE((byte)0), IN_CONFERENCE((byte)1);
	
	private byte code;
	
	private ConfStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ConfStatus fromStatus(byte code) {
		for(ConfStatus v : ConfStatus.values()) {
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
