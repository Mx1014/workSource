package com.everhomes.rest.organization.pm;

/**
 * <ul>
 * <li>LIVING : 1, 按天</li>
 * <li>RENT : 2, 按月</li>
 * <li>FREE : 3, 按季</li>
 * <li>SALED : 4, 按年</li>
 * </ul>
 */
public enum AuthorizePriceType {
	ERRORSTATUS((byte) -1, "无效状态"), DAY((byte) 1, "按天"), MONTH((byte) 2, "按月"), QUARTER((byte) 3, "按季"), YEAR((byte) 4, "按年");

	private byte code;
	private String desc;

	private AuthorizePriceType(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public byte getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public static AuthorizePriceType fromCode(Byte code) {
		if (code != null) {
			for (AuthorizePriceType status : AuthorizePriceType.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}

	public static AuthorizePriceType fromDesc(String desc) {
		if (desc != null && !desc.isEmpty()) {
			for (AuthorizePriceType status : AuthorizePriceType.values()) {
				if (status.desc.equals(desc)) {
					return status;
				}
			}
		}
		return AuthorizePriceType.ERRORSTATUS;
	}
}