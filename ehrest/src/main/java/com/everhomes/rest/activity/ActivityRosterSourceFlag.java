// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>SELF: 1，自发</li>
 * <li>BACKEND_ADD: 2，后台录入</li>
 * </ul>
 */
public enum ActivityRosterSourceFlag {
	SELF((byte) 1), BACKEND_ADD((byte) 2);
	private byte code;

	private ActivityRosterSourceFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ActivityRosterSourceFlag fromCode(Byte code) {
		if (code != null) {
			for (ActivityRosterSourceFlag flag : ActivityRosterSourceFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
