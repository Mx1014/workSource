// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>ACTIVITYROSTER_CONFIRM: 1，confirm</li>
 * <li>ACTIVITYROSTER_REJECT: 2，reject</li>
 * <li>ACTIVITYROSTER_UNCONFIRM: 0，unconfirm</li>
 * </ul>
 */

public enum ActivityRosterStatusFlag {
	ACTIVITYROSTER_UNCONFIRM((byte)0, "unconfirm"), ACTIVITYROSTER_CONFIRM((byte)1, "confirm"), ACTIVITYROSTER_REJECT((byte)0, "reject");
	
	private byte code;
	private String text;
	
	private ActivityRosterStatusFlag(byte code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	public String getText() {
		return text;
	}
	
	public static ActivityRosterStatusFlag fromCode(Byte code) {
		if (code != null) {
			for (ActivityRosterStatusFlag activityRosterStatusFlag : ActivityRosterStatusFlag.values()) {
				if (activityRosterStatusFlag.getCode() == code.byteValue()) {
					return activityRosterStatusFlag;
				}
			}
		}
		return null;
	}
}