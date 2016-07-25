package com.everhomes.rest.officecubicle;

public enum OfficeSpaceType {
	VISIT((byte) 1, "个"), ORDER((byte)  2, "㎡");

	
	private byte code;
	private String msg;

	private OfficeSpaceType(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public byte getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static OfficeSpaceType fromCode(byte code) {
		for (OfficeSpaceType t : OfficeSpaceType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
