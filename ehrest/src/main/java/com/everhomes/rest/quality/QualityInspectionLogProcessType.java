package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>0: none, 1: insert, 2: update, 3: delete</li>
 * </ul>
 */
public enum QualityInspectionLogProcessType {

	NONE((byte)0), INSERT((byte)1), UPDATE((byte)2), DELETE((byte)3);
	
	private Byte code;
	
	private QualityInspectionLogProcessType(Byte code){
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static QualityInspectionLogProcessType fromStatus(Byte code) {
		for(QualityInspectionLogProcessType v : QualityInspectionLogProcessType.values()) {
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
