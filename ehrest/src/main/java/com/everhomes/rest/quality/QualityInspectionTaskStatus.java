package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>WAITING_FOR_EXECUTING : 1</li>
 *	<li>RECTIFING : 2</li>
 *	<li>RECTIFIED_AND_WAITING_APPROVAL : 3</li>
 *	<li>RECTIFY_CLOSED_AND_WAITING_APPROVAL : 4</li>
 *	<li>CLOSED : 5</li>
 * </ul>
 */
public enum QualityInspectionTaskStatus {

	NONE((byte)0), WAITING_FOR_EXECUTING((byte)1), RECTIFING((byte)2), RECTIFIED_AND_WAITING_APPROVAL((byte)3),
	RECTIFY_CLOSED_AND_WAITING_APPROVAL((byte)4), CLOSED((byte)5);
	
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
