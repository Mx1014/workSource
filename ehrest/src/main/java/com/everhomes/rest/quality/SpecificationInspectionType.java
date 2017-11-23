package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * 
 * 规范类型 0: 类型, 1: 规范, 2: 规范事项
 *
 */
public enum SpecificationInspectionType {

	CATEGORY((byte)0), SPECIFICATION((byte)1), SPECIFICATION_ITEM((byte)2);
	
	private Byte code;
	
	private SpecificationInspectionType(Byte code){
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static SpecificationInspectionType fromStatus(Byte code) {
		for(SpecificationInspectionType v : SpecificationInspectionType.values()) {
			if(v.getCode().equals(code))
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
