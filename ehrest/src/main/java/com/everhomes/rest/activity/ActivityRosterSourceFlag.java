// @formatter:off
package com.everhomes.rest.activity;

/**
 * 
 * <ul>
 * <li>SELF: 1，自发</li>
 * <li>BACKEND_ADD: 2，后台录入</li>
 * <li>WECHAT: 3，微信报名</li>
 * </ul>
 */
public enum ActivityRosterSourceFlag {
	SELF((byte) 1, "自发报名"), BACKEND_ADD((byte) 2, "后台录入"), WECHAT((byte) 3, "微信报名");
	private byte code;
	private String text;

	private ActivityRosterSourceFlag(byte code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getText() {
		return text;
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
		return SELF;
	}
}
