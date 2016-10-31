package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: none</li>
 *  <li>1: qualified</li>
 *  <li>2: unqualified</li>
 *  <li>3: inactive</li>
 *  <li>4: REVIEW_DELAY</li>
 * </ul>
 */
public enum ReviewResult {
	NONE((byte)0), QUALIFIED((byte)1), UNQUALIFIED((byte)2), INACTIVE((byte)3), REVIEW_DELAY((byte)4);
	
	private byte code;
	
	private ReviewResult(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ReviewResult fromStatus(byte code) {
		for(ReviewResult v : ReviewResult.values()) {
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
