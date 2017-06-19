// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>PRINT((byte)1) : 打印</li>
 * <li>COPY((byte)2) : 复印</li>
 * <li>SCAN((byte)3) : 扫描</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintJobTypeType {
	PRINT((byte)1),COPY((byte)2),SCAN((byte)3);
	
	private byte code;

	private PrintJobTypeType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PrintJobTypeType fromCode(byte code) {
		for (PrintJobTypeType t : PrintJobTypeType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
