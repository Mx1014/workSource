package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>EXECUTIVE_GROUP : 1</li>
 *	<li>REVIEW_GROUP : 2</li>0: none, 1: inspect, 2: retify, 3: review, 4: assgin, 5: forward
 * </ul>
 */
public enum ProcessType {
	NONE((byte)0), INSPECT((byte)1), RETIFY((byte)2), REVIEW((byte)3), ASSIGN((byte)4), FORWARD((byte)5);
	
	private byte code;
	
	private ProcessType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static ProcessType fromStatus(byte code) {
		for(ProcessType v : ProcessType.values()) {
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
