// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>SEND_ADDRESS: 1，寄件人地址</li>
 * <li>RECEIVE_ADDRESS: 2，收件人地址</li>
 * </ul>
 */
public enum ExpressAddressCategory {
	SEND_ADDRESS((byte)1), RECEIVE_ADDRESS((byte)2);
	
	private byte code;
	
	private ExpressAddressCategory(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public static ExpressAddressCategory fromCode(Byte code) {
		if (code != null) {
			for (ExpressAddressCategory expressAddressCategory : ExpressAddressCategory.values()) {
				if (expressAddressCategory.getCode().byteValue() == code.byteValue()) {
					return expressAddressCategory;
				}
			}
		}
		return null;
	} 
}
