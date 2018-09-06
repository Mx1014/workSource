//@formatter:off
package com.everhomes.rest.express;

/**
* 
* <ul>
* <li>GOODS: 1，物品</li>
* <li>FILE: 2，文件</li>
* <li>OTHER: 3，其他</li>
* </ul>
*/
public enum ExpressTypeType {
	GOODS((byte)0,"物品"), FILE((byte)1,"文件"), OTHER((byte)2,"其他");

	private byte code;
	private String description;

	private ExpressTypeType(byte code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static ExpressTypeType fromCode(Byte code) {
		if (code != null) {
			for (ExpressTypeType expressInvoiceFlagType : ExpressTypeType.values()) {
				if (expressInvoiceFlagType.getCode().byteValue() == code.byteValue()) {
					return expressInvoiceFlagType;
				}
			}
		}
		return null;
	} 
}
