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
	AUTH((byte) 1, "认证"), NOT_AUTH((byte) 2, "非认证"), NOT_REGISTER((byte) 3, "非注册");
	private byte code;
	private String text;

	private UserAuthFlag(byte code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getText() {
		return text;
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
