package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>NONE : 0</li>
 *	<li>INSPECT : 1</li>
 *	<li>RECTIFY : 2</li>
 *	<li>REVIEW : 3</li>
 *	<li>ASSIGN : 4</li>
 *	<li>FORWARD : 5</li>
 * </ul>
 */
public enum QualityInspectionTaskProcessType {
	NONE((byte)0), INSPECT((byte)1), RECTIFY((byte)2), 
	REVIEW((byte)3), ASSIGN((byte)4), FORWARD((byte)5);
	
	private byte code;
	
	private QualityInspectionTaskProcessType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QualityInspectionTaskProcessType fromStatus(byte code) {
		for(QualityInspectionTaskProcessType v : QualityInspectionTaskProcessType.values()) {
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
