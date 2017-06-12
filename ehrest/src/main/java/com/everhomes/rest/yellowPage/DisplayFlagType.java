// @formatter:off
package com.everhomes.rest.yellowPage;

/**
 * 
 * <ul>
 * <li>HIDE((byte)0) : 在app端隐藏服务联盟企业</li>
 * <li>SHOW((byte)1) : 在app端显示服务联盟企业</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public enum DisplayFlagType {

	SHOW((byte)1), HIDE((byte)0);
	
	private byte code;
	
	private DisplayFlagType(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	
	public static DisplayFlagType fromCode(Byte code) {
		if (code != null) {
			for (DisplayFlagType showOrHideType : DisplayFlagType.values()) {
				if (showOrHideType.getCode() == code.byteValue()) {
					return showOrHideType;
				}
			}
		}
		return null;
	}
}
