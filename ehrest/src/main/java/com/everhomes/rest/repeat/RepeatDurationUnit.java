package com.everhomes.rest.repeat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>MINUTE : m</li>
 *	<li>HOUR : h</li>
 *	<li>DAY : d</li>
 *	<li>WEEK : W</li>
 *	<li>MONTH : M</li>
 *	<li>YEAR : Y</li>
 * </ul>
 */
public enum RepeatDurationUnit {


	MINUTE("m"), HOUR("h"), DAY("d"), WEEK("W"), MONTH("M"), YEAR("Y");
	
	private String code;
	
	private RepeatDurationUnit(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static RepeatDurationUnit fromStatus(String code) {
		for(RepeatDurationUnit v : RepeatDurationUnit.values()) {
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
