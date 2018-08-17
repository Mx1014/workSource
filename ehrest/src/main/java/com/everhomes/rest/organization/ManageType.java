package com.everhomes.rest.organization;

/**
 * 
 * <ul>
 * <li>ENTERPRISE: 0 企业管理员</li>
 * </ul>
 */
public enum ManageType {
	ENTERPRISE((byte)0, "企业管理员");

	private Byte code;
	private String text;

	private ManageType(byte code, String text) {
		this.code = code;
		this.text = text;
	}
	
	public Byte getCode() {
		return this.code;
	}
	
	public String getText() {
		return text;
	}
	
	public static ManageType fromCode(Byte code) {
		if (code != null) {
			for (ManageType trueOrFalseFlag : ManageType.values()) {
				if (trueOrFalseFlag.getCode().equals(code)) {
					return trueOrFalseFlag;
				}
			}
		}
		return null;
	}
}
