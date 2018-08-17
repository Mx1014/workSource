//@formatter:off
package com.everhomes.rest.express;

/**
 *
 * <ul>
 * <li>LAND_CARRIAGE: 0，陆运</li>
 * <li>AIR_TRANSPORTATION: 1，空运</li>
 * </ul>
 */
public enum ExpressWayType {
	LAND_CARRIAGE((byte)0,"陆运"), AIR_TRANSPORTATION ((byte)1,"空运");

	private byte code;
	private String description;

	private ExpressWayType(byte code, String description) {
		this.code = code;
		this.description = description;
	}

	public Byte getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static ExpressWayType fromCode(Byte code) {
		if (code != null) {
			for (ExpressWayType expressInvoiceFlagType : ExpressWayType.values()) {
				if (expressInvoiceFlagType.getCode().byteValue() == code.byteValue()) {
					return expressInvoiceFlagType;
				}
			}
		}
		return null;
	}
}
