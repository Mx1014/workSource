package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>STANDARD : standard</li>
 * </ul>
 */
public enum QualityInspectionLogType {
	STANDARD("standard");
	
	private String code;
	
	private QualityInspectionLogType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static QualityInspectionLogType fromStatus(String code) {
		for(QualityInspectionLogType v : QualityInspectionLogType.values()) {
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
