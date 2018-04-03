package com.everhomes.rest.openapi;

public enum BizMessageType {
	TEXT((byte)1, "电商文本消息"),
	VOICE((byte)2, "电商语音提醒消息");
	
	private byte code;
	private String msg;
	
	private BizMessageType(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public static BizMessageType fromCode(Byte code) {
		if(code == null) {
			return null;
		}
		switch(code.byteValue()) {	
		case (byte)1:
			return TEXT;
		case (byte)2:
			return VOICE;
		default:
			return null;
		}
	}

	public byte getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
