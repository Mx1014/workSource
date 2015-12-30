package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 0 –email	1 –message	2 -wechat
 *
 */
public enum InvitationType {

	EMAIL((byte)0), MESSAGE((byte)1), WECHAT((byte)2);
	
	private byte code;
	
	private InvitationType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static InvitationType fromType(byte code) {
		for(InvitationType v : InvitationType.values()) {
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
