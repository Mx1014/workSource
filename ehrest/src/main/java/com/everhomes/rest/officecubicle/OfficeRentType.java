package com.everhomes.rest.officecubicle;

public enum OfficeRentType {
	OPENSITE((byte) 1, "开放式工位"), WHOLE((byte)  2, "整租空间");

	
	private byte code;
	private String msg;

	private OfficeRentType(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public byte getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static OfficeRentType fromCode(byte code) {
		for (OfficeRentType t : OfficeRentType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
