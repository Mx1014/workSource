// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>CASH: 1，寄付现结</li>
 * <li>OFFLINE: 2，线下支付</li>
 * </ul>
 */
public enum ExpressPayType {
	CASH((byte)1),OFFLINE((byte)2);
	
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
