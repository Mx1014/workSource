package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>FALSE : 0</li>
 *	<li>TRUE : 1</li>
 * </ul>
 *
 */
public enum FeedbackVerifyType {
	FALSE((byte)0), TRUE((byte)1);
	
	private byte code;
	
	private FeedbackVerifyType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static FeedbackVerifyType fromStatus(byte code) {
		for(FeedbackVerifyType v : FeedbackVerifyType.values()) {
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
