package com.everhomes.rest.launchpad;

/**
 * <ul>
 * <li>NO(非定制): 0</li>
 * <li>YES(定制): 1</li>
 * </ul>
 */
public enum CssStyleFlagType {
	NO((byte)0),YES((byte)1);
	private byte code;
	private CssStyleFlagType(byte code){
		this.code = code;
	}
	public byte getCode() {
		return code;
	}
	public static CssStyleFlagType fromCode(byte code){
		for(CssStyleFlagType r: CssStyleFlagType.values()){
			if(r.getCode() == code)
				return r;
		}
		return null;
	}

}
