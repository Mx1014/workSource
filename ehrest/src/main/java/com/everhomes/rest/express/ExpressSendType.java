// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>STANDARD: 1，标准快递</li>
 * </ul>
 */
public enum ExpressSendType {
	STANDARD((byte)1);
	
	private byte code;
	
	private ExpressSendType(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressSendType fromCode(Byte code) {
		if (code != null) {
			for (ExpressSendType expressSendType : ExpressSendType.values()) {
				if (expressSendType.getCode().byteValue() == code.byteValue()) {
					return expressSendType;
				}
			}
		}
		return null;
	} 
}
