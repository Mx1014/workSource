package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>QUALIFIED : 1</li>
 *	<li>UNQUALIFIED : 2</li>
 * </ul>
 */
public enum QualityInspectionTaskReviewResult {

	NONE((byte)0), QUALIFIED((byte)1), UNQUALIFIED((byte)2);
	
	private byte code;
	
	private QualityInspectionTaskReviewResult(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityInspectionTaskReviewResult fromStatus(byte code) {
		for(QualityInspectionTaskReviewResult v : QualityInspectionTaskReviewResult.values()) {
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
