// @formatter:off
package com.everhomes.rest.poll;

/**
 * 
 * <ul>
 * <li>NO: 0</li>
 * <li>YES: 1</li>
 * </ul>
 */
public enum WechatPollFlag {
	NO((byte) 0), YES((byte) 1);
	private byte code;

	private WechatPollFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static WechatPollFlag fromCode(Byte code) {
		if (code != null) {
			for (WechatPollFlag flag : WechatPollFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return NO;
	}
}
