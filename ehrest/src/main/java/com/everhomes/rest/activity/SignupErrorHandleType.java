// @formatter:off
package com.everhomes.rest.activity;

/**
 * <ul>
 *     <li>SKIP((byte) 0): SKIP</li>
 *     <li>UPDATE((byte) 1): UPDATE</li>
 * </ul>
 */
public enum SignupErrorHandleType {
	SKIP((byte) 0), UPDATE((byte) 1);
	private byte code;

	private SignupErrorHandleType(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static SignupErrorHandleType fromCode(Byte code) {
		if (code != null) {
			for (SignupErrorHandleType flag : SignupErrorHandleType.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
