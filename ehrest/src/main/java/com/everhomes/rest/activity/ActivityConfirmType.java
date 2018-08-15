// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>NOT_NEED_CONFIRM: 0 不需要确认</li>
 * <li>NEED_CONFIRM: 1 需要确认</li>
 * </ul>
 */
public enum ActivityConfirmType {
	NOT_NEED_CONFIRM((byte) 0), NEED_CONFIRM((byte) 1);
	private byte code;

	private ActivityConfirmType(byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static ActivityConfirmType fromCode(Byte code) {
		if (code != null) {
			for (ActivityConfirmType flag : ActivityConfirmType.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return NOT_NEED_CONFIRM;
	}
}
