// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>CASH: 1，寄付现结</li>
 * </ul>
 */
public enum ExpressPayType {
	CASH((byte)1);
	
	private byte code;
	
	private ExpressPayType(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressPayType fromCode(Byte code) {
		if (code != null) {
			for (ExpressPayType expressPayType : ExpressPayType.values()) {
				if (expressPayType.getCode().byteValue() == code.byteValue()) {
					return expressPayType;
				}
			}
		}
		return null;
	} 
}
