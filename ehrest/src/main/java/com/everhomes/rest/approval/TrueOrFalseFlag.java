package com.everhomes.rest.approval;

/**
 * 
 * <ul>true false
 * <li>TRUE: 1</li>
 * <li>FALSE: 0</li>
 * </ul>
 */
public enum TrueOrFalseFlag {
	TRUE((byte)1), FALSE((byte)0);
	
	private byte code;
	
	private TrueOrFalseFlag(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return this.code;
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
