package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>CORRECT : 1</li>
 *	<li>INSPECT_COMPLETE : 2</li>
 *	<li>CORRECT_COMPLETE : 3</li>
 *	<li>INSPECT_DELAY : 4</li>
 *	<li>CORRECT_DELAY : 5</li>
 * </ul>
 */
public enum QualityInspectionTaskResult {
	NONE((byte)0), CORRECT((byte)1), INSPECT_COMPLETE((byte)2), CORRECT_COMPLETE((byte)3),
	INSPECT_DELAY((byte)4), CORRECT_DELAY((byte)5);
	
	private byte code;
	
	private QualityInspectionTaskResult(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityInspectionTaskResult fromStatus(byte code) {
		for(QualityInspectionTaskResult v : QualityInspectionTaskResult.values()) {
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
