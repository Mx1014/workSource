package com.everhomes.rest.approval;

/**
 * 
 * <ul>true false
 * <li>TRUE: 1</li>
 * <li>FALSE: 0</li>
 * </ul>
 */
public enum TrueOrFalseFlag {
	TRUE((byte)1, "是"), FALSE((byte)0, "否");
	
	private byte code;
	private String text;
	
	private TrueOrFalseFlag(byte code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public byte getCode() {
		return this.code;
	}
	
	public String getText() {
		return text;
	}
	
	public static TrueOrFalseFlag fromCode(Byte code) {
		if (code != null) {
			for (TrueOrFalseFlag trueOrFalseFlag : TrueOrFalseFlag.values()) {
				if (trueOrFalseFlag.getCode() == code.byteValue()) {
					return trueOrFalseFlag;
				}
			}
		}
		return null;
	}
}
