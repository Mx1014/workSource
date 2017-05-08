// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>UNPAY: 0</li>
 * <li>PAY: 1</li>
 * <li>TOBEPAY: 2</li>
 * <li>REFUND: 2</li>
 * </ul>
 */
public enum ActivityRosterPayFlag {
	UNPAY((byte) 0), PAY((byte) 1), TOBEPAY((byte)2), REFUND((byte)3);
	private byte code;

	private ActivityRosterPayFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ActivityRosterPayFlag fromCode(Byte code) {
		if (code != null) {
			for (ActivityRosterPayFlag flag : ActivityRosterPayFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return UNPAY;
	}
}
