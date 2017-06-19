package com.everhomes.rest.general_approval;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>DATE: 日期</li>
 * <li>DATETIME: 日期+时间</li>
 * <li>TIME: 时间</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormDateType {
	DATE("DATE"),DATETIME("DATETIME"),TIME("TIME");
	
	private String code;
	
	private GeneralFormDateType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormDateType fromCode(String code) {
		for(GeneralFormDateType v : GeneralFormDateType.values()) {
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
