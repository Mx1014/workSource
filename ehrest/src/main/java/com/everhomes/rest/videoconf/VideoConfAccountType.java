package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public enum VideoConfAccountType {
	
	VIDEO_ONLY_25((byte)0), PHONE_SUPPORT_25((byte)1), VIDEO_ONLY_100((byte)2), PHONE_SUPPORT_100((byte)3);
	
	private byte code;
	
	private VideoConfAccountType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static VideoConfAccountType fromType(byte code) {
		for(VideoConfAccountType v : VideoConfAccountType.values()) {
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
