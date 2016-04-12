package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>REVIEWED : 1</li>
 * </ul>
 */
public enum QualityInspectionTaskReviewStatus {

	NONE((byte)0), REVIEWED((byte)1);
	
	private byte code;
	
	private QualityInspectionTaskReviewStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityInspectionTaskReviewStatus fromStatus(byte code) {
		for(QualityInspectionTaskReviewStatus v : QualityInspectionTaskReviewStatus.values()) {
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
