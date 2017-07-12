package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>EQUIPMENT : equipment 设备</li>
 * </ul>
 *
 */
public enum InspectionStandardMapTargetType {

	EQUIPMENT("equipment");
	
	private String code;
	
	private InspectionStandardMapTargetType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static InspectionStandardMapTargetType fromStatus(String code) {
		for(InspectionStandardMapTargetType v : InspectionStandardMapTargetType.values()) {
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
