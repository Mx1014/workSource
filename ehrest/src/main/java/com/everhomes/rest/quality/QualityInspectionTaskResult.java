package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>INSPECT_OK : 1</li>
 *	<li>INSPECT_CLOSE : 2</li>
 *	<li>RECTIFIED_OK : 3</li>
 *	<li>RECTIFY_CLOSED : 4</li>
 *	<li>INSPECT_DELAY : 5</li>
 *	<li>RECTIFY_DELAY : 6</li>
 *	<li>CORRECT_DELAY : 7</li>
 *	<li>RECTIFIED_OK_AND_WAITING_APPROVAL : 11</li>
 *	<li>RECTIFY_CLOSED_AND_WAITING_APPROVAL : 12</li>
 * </ul>
 */
public enum QualityInspectionTaskResult {
	NONE((byte)0), INSPECT_OK((byte)1), INSPECT_CLOSE((byte)2), RECTIFIED_OK((byte)3),
	RECTIFY_CLOSED((byte)4), INSPECT_DELAY((byte)5), RECTIFY_DELAY((byte)6),  CORRECT_DELAY((byte)7), 
	RECTIFIED_OK_AND_WAITING_APPROVAL((byte)11), RECTIFY_CLOSED_AND_WAITING_APPROVAL((byte)12);
	
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
