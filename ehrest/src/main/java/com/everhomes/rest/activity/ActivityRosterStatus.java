// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>CANCEL: 0</li>
 * <li>REJECT: 1</li>
 * <li>NORMAL: 2</li>
 * </ul>
 */
public enum ActivityRosterStatus {
	CANCEL((byte) 0), REJECT((byte) 1), NORMAL((byte)2);
	private byte code;

	private ActivityRosterStatus(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ActivityRosterStatus fromCode(Byte code) {
		if (code != null) {
			for (ActivityRosterStatus flag : ActivityRosterStatus.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return NORMAL;
	}
}
