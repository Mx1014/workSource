// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>PRINT_COPY_SCAN((byte)1): 打印复印扫描价格</li>
 * <li>COURSE_HOTLINE((byte)2) : 教程/热线</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintSettingType {
	PRINT_COPY_SCAN((byte)1),COURSE_HOTLINE((byte)2);
	
	private byte code;

	private PrintSettingType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PrintSettingType fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintSettingType t : PrintSettingType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
