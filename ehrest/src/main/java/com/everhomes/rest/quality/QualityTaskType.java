package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>VERIFY_TASK : 1</li>
 *	<li>RECTIFY_TASK : 2</li>
 * </ul>
 */
public enum QualityTaskType {

	NONE((byte)0), VERIFY_TASK((byte)1), RECTIFY_TASK((byte)2);
	
	private byte code;
	
	private QualityTaskType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityTaskType fromStatus(byte code) {
		for(QualityTaskType v : QualityTaskType.values()) {
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
