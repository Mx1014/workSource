package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>EXECUTIVE_GROUP : 1</li>
 *	<li>REVIEW_GROUP : 2</li>
 * </ul>
 */
public enum QualityGroupType {
	NONE((byte)0), EXECUTIVE_GROUP((byte)1), REVIEW_GROUP((byte)2);
	
	private byte code;
	
	private QualityGroupType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityGroupType fromStatus(byte code) {
		for(QualityGroupType v : QualityGroupType.values()) {
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
