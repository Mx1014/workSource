// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>NO: 0</li>
 * <li>YES: 1</li>
 * </ul>
 */
public enum WechatSignupFlag {
	NO((byte) 0), YES((byte) 1);
	private byte code;

	private WechatSignupFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static WechatSignupFlag fromCode(Byte code) {
		if (code != null) {
			for (WechatSignupFlag flag : WechatSignupFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return NO;
	}
}
