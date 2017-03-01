// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>AUTH: 1，认证</li>
 * <li>NOT_AUTH: 2， 非认证</li>
 * <li>NOT_REGISTER: 3，非注册</li>
 * </ul>
 */
public enum UserAuthFlag {
	AUTH((byte) 1), NOT_AUTH((byte) 2), NOT_REGISTER((byte) 3);
	private byte code;

	private UserAuthFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static UserAuthFlag fromCode(Byte code) {
		if (code != null) {
			for (UserAuthFlag flag : UserAuthFlag.values()) {
				if (flag.code == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
