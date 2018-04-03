package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>NONE : 0</li>
 *	<li>DELETE : 1</li>
 * </ul>
 *
 */
public enum FeedbackHandleType {
	NONE((byte)0), DELETE((byte)1);
	
	private byte code;
	
	private FeedbackHandleType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static FeedbackHandleType fromStatus(byte code) {
		for(FeedbackHandleType v : FeedbackHandleType.values()) {
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
