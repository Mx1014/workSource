package com.everhomes.rest.officecubicle;

public enum OfficeStatus {
	DELETED((byte) 0, "已删除"), NORMAL((byte)  2, "普通");

	
	private byte code;
	private String msg;

	private OfficeStatus(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public byte getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static OfficeStatus fromCode(byte code) {
		for (OfficeStatus t : OfficeStatus.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
