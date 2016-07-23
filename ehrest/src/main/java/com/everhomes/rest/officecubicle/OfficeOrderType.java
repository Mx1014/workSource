package com.everhomes.rest.officecubicle;

public enum OfficeOrderType {
	VISIT((byte) 0, "预约参观"), ORDER((byte)  1, "工位续订");

	private byte code;
	private String msg;

	private OfficeOrderType(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static OfficeOrderType fromCode(byte code) {
		for (OfficeOrderType t : OfficeOrderType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
