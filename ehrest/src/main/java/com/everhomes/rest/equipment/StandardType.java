package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: none</li>
 *  <li>1: routing inspection 巡检</li>
 *  <li>2:maintain 保养</li>
 * </ul>
 */
public enum StandardType {

	NONE((byte)0, ""), ROUTING_INSPECTION((byte)1, "巡检"), MAINTAIN((byte)2, "保养");
	
	private byte code;
	private String name;
	
	private StandardType(byte code, String name){
		this.code = code;
		this.name = name;
	}
	
	public byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public static StandardType fromStatus(byte code) {
		for(StandardType v : StandardType.values()) {
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
