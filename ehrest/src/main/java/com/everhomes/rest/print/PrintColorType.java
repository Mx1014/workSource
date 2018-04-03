// @formatter:off
package com.everhomes.rest.print;

/**
 * 
 * <ul>
 * <li>BLACK_WHITE((byte)0) : 黑白</li>
 * <li>COLOR((byte)1) : 彩色</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintColorType {
    
    BLACK_WHITE((byte)0,"黑白"),COLOR((byte)1,"彩色");
	
	private byte code;
	private String desc;

	private PrintColorType(byte code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}

	public byte getCode() {
		return code;
	}

	public static PrintColorType fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintColorType t : PrintColorType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
