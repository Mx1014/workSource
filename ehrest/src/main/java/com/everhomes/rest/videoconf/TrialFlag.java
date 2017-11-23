package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * REJECT: 0
 * OK: 2
 *
 */
public enum TrialFlag {

	REJECT((byte)0), OK((byte)2);
	
	private byte code;
	
	private TrialFlag(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static TrialFlag fromStatus(byte code) {
		for(TrialFlag v : TrialFlag.values()) {
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
