//@formatter:off
package com.everhomes.rest.express;

/**
* 
* <ul>
* <li>NOT_NEED_INVOICE: 1，不需要发票</li>
* <li>NEED_TEAR_INVOICE: 2，需要手撕发票</li>
* <li>NEED_TAX_INVOIE: 3，需要税票</li>
* </ul>
*/
public enum ExpressInvoiceFlagType {
	NOT_NEED_INVOICE((byte)1,"不需要发票"), NEED_TEAR_INVOICE((byte)2,"需要手撕发票"), NEED_TAX_INVOIE((byte)3,"需要税票");
	
	private byte code;
	private String description;
	
	private ExpressInvoiceFlagType(byte code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static ExpressInvoiceFlagType fromCode(Byte code) {
		if (code != null) {
			for (ExpressInvoiceFlagType expressInvoiceFlagType : ExpressInvoiceFlagType.values()) {
				if (expressInvoiceFlagType.getCode().byteValue() == code.byteValue()) {
					return expressInvoiceFlagType;
				}
			}
		}
		return null;
	} 
}
