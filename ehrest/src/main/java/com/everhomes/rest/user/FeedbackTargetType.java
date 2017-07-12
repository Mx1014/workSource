package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>POST : 1</li>
 *  <li>ADDRESS : 2</li>
 *  <li>FORUM : 3</li>
 * </ul>
 *
 */
public enum FeedbackTargetType {
	NONE((byte)0), POST((byte)1), ADDRESS((byte)2), FORUM((byte)3);
	
	private byte code;
	
	private FeedbackTargetType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static FeedbackTargetType fromStatus(byte code) {
		for(FeedbackTargetType v : FeedbackTargetType.values()) {
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
