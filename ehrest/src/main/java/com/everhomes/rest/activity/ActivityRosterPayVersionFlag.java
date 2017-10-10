// @formatter:off
package com.everhomes.rest.activity;

/**
 * <ul>
 *     <li>V1((byte) 1): V1</li>
 *     <li>V2((byte)2): V2</li>
 * </ul>
 */
public enum ActivityRosterPayVersionFlag {
	V1((byte) 1), V2((byte) 2);
	private byte code;

	private ActivityRosterPayVersionFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ActivityRosterPayVersionFlag fromCode(Byte code) {
		if (code != null) {
			for (ActivityRosterPayVersionFlag flag : ActivityRosterPayVersionFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return V1;
	}
}
