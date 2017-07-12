// @formatter:off
package com.everhomes.rest.yellowPage;

import com.everhomes.rest.approval.TrueOrFalseFlag;
/**
 * 
 * <ul>
 * <li>TRUE(1) : 折叠描述信息</li>
 * <li>FALSE(0) : 不折叠描述信息</li>
 * </ul>
 *
 *  @author:dengs 2017年5月11日
 */
public enum CollapseFlag {
	TRUE((byte)1), FALSE((byte)0);
	
	private byte code;
	
	private CollapseFlag(Byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	public static TrueOrFalseFlag fromCode(Byte code) {
		if (code != null) {
			for (TrueOrFalseFlag trueOrFalseFlag : TrueOrFalseFlag.values()) {
				if (trueOrFalseFlag.getCode() == code.byteValue()) {
					return trueOrFalseFlag;
				}
			}
		}
		return null;
	}
}
