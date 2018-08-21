//@formatter:off
package com.everhomes.rest.express;

/**
* 
* <ul>
* <li>THE_SAME_CITY: 0，同城</li>
* <li>OTHER_CITIES: 1，外埠</li>
* </ul>
*/
public enum ExpressTargetType {
	THE_SAME_CITY((byte)0,"同城"), OTHER_CITIES((byte)1,"外埠");

	private byte code;
	private String description;

	private ExpressTargetType(byte code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static ExpressTargetType fromCode(Byte code) {
		if (code != null) {
			for (ExpressTargetType expressInvoiceFlagType : ExpressTargetType.values()) {
				if (expressInvoiceFlagType.getCode().byteValue() == code.byteValue()) {
					return expressInvoiceFlagType;
				}
			}
		}
		return null;
	} 
}
