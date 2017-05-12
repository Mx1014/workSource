package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>NO(非定制): 0</li>
 * <li>YES(定制): 1</li>
 * </ul>
 */
public enum CustomFlagType {
	NO((byte)0),YES((byte)1);
	private byte code;
	private CustomFlagType(byte code){
		this.code = code;
	}
	public byte getCode() {
		return code;
	}
	public static CustomFlagType fromCode(byte code){
		for(CustomFlagType r: CustomFlagType.values()){
			if(r.getCode() == code)
				return r;
		}
		return null;
	}

}
