// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>SELF: 1，服务点自寄</li>
 * </ul>
 */
public enum ExpressSendMode {
	SELF((byte)1);
	
	private byte code;
	
	private ExpressSendMode(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressSendMode fromCode(Byte code) {
		if (code != null) {
			for (ExpressSendMode expressSendMode : ExpressSendMode.values()) {
				if (expressSendMode.getCode().byteValue() == code.byteValue()) {
					return expressSendMode;
				}
			}
		}
		return null;
	} 
}
