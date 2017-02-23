package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * 
 * 规范类型 0: 类型, 1: 规范, 2: 规范事项
 *
 */
public enum SpecificationInspectionType {

	CATEGORY((byte)0), SPECIFICATION((byte)1), SPECIFICATION_ITEM((byte)2);
	
	private byte code;
	
	private SpecificationInspectionType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static SpecificationInspectionType fromStatus(byte code) {
		for(SpecificationInspectionType v : SpecificationInspectionType.values()) {
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
