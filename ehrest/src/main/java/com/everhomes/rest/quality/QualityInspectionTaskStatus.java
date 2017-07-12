package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>待执行WAITING_FOR_EXECUTING : 1</li>
 *	<li>已执行EXECUTED : 2</li>
 *	<li>已延误DELAY : 3</li>
 * </ul>
 */
public enum QualityInspectionTaskStatus {

	NONE((byte)0), WAITING_FOR_EXECUTING((byte)1), EXECUTED((byte)2), DELAY((byte)3);
	
	private byte code;
	
	private QualityInspectionTaskStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityInspectionTaskStatus fromStatus(byte code) {
		for(QualityInspectionTaskStatus v : QualityInspectionTaskStatus.values()) {
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
