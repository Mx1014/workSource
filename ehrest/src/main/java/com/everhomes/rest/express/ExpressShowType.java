// @formatter:off
package com.everhomes.rest.express;
/**
 * 
 * <ul>
 * <li>HIDDEN : 0, 不显示</li>
 * <li>SHOW : 1, 显示</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public enum ExpressShowType {
	HIDDEN((byte)0),
	SHOW((byte)1);
	
	private byte code;
	
	private ExpressShowType(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressShowType fromCode(Byte code) {
		if (code != null) {
			for (ExpressShowType expressSettingShowType : ExpressShowType.values()) {
				if (expressSettingShowType.getCode().byteValue() == code.byteValue()) {
					return expressSettingShowType;
				}
			}
		}
		return null;
	} 


}
