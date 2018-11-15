package com.everhomes.rest.common;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <ul>true false
 * <li>TRUE: 1</li>
 * <li>FALSE: 0</li>
 * </ul>
 */
public enum TrueOrFalseFlag {
	TRUE((byte)1, "是"), FALSE((byte)0, "否");
	
	private Byte code;
	private String text;
	
	private TrueOrFalseFlag(byte code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public Byte getCode() {
		return this.code;
	}
	
	public String getText() {
		return text;
	}
	
	public static TrueOrFalseFlag fromCode(Byte code) {
		if (code != null) {
			for (TrueOrFalseFlag trueOrFalseFlag : TrueOrFalseFlag.values()) {
				if (trueOrFalseFlag.getCode().equals(code)) {
					return trueOrFalseFlag;
				}
			}
		}
		return null;
	}

	public static TrueOrFalseFlag fromText(String text) {
		if (!StringUtils.isBlank(text)) {
			for (TrueOrFalseFlag trueOrFalseFlag : TrueOrFalseFlag.values()) {
				if (trueOrFalseFlag.getText().equals(text)) {
					return trueOrFalseFlag;
				}
			}
		}
		return TRUE;
	}
}
