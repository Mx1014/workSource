// @formatter:off
package com.everhomes.rest.me_menu;

/**
 * <ul>
 *     <li>NORMAL((byte) 1): 菜单的位置-上面部分</li>
 *     <li>BOTTOM((byte)2): 菜单的位置-底部</li>
 * </ul>
 */
public enum PositionFlag {
	NORMAL((byte) 1), BOTTOM((byte) 2);
	private byte code;

	private PositionFlag(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PositionFlag fromCode(Byte code) {
		if (code != null) {
			for (PositionFlag flag : PositionFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
