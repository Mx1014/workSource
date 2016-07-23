package com.everhomes.rest.officecubicle;

public enum OfficeOrderStatus {
	NORMAL((byte) 0, "用户客户端可见"), UNVISABLE((byte) -1, "用户客户端不可见");

	private byte code;
	private String msg;

	private OfficeOrderStatus(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static OfficeOrderStatus fromCode(byte code) {
		for (OfficeOrderStatus t : OfficeOrderStatus.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
