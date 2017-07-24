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
	NOT_NEED_INVOICE((byte)1), NEED_TEAR_INVOICE((byte)2), NEED_TAX_INVOIE((byte)3);
	
	private byte code;
	
	private ExpressInvoiceFlagType(byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return code;
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
