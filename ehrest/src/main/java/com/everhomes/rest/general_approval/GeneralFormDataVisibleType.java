package com.everhomes.rest.general_approval;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

public enum GeneralFormDataVisibleType {
	HIDDEN("HIDDEN"), READONLY("READONLY"), EDITABLE("EDITABLE");
	
	private String code;
	
	private GeneralFormDataVisibleType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormDataVisibleType fromCode(String code) {
		for(GeneralFormDataVisibleType v : GeneralFormDataVisibleType.values()) {
			if(StringUtils.equals(v.getCode(), code))
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
