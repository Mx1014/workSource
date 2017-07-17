// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>UNKNOWN: 0</li>
 * <li>WECHAT: 1</li>
 * </ul>
 */
public enum SignupSourceFlag {
	UNKNOWN((byte) 0), WECHAT((byte) 1);
	private byte code;

	private SignupSourceFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static SignupSourceFlag fromCode(Byte code) {
		if (code != null) {
			for (SignupSourceFlag flag : SignupSourceFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return UNKNOWN;
	}
}
