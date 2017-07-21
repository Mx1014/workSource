// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>SELF: 1，服务点自寄</li>
 * <li>VISIT: 2，快递员上门收件</li>
 * </ul>
 */
public enum ExpressSendMode {
	SELF((byte)1),
	VISIT((byte)2);
	
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
