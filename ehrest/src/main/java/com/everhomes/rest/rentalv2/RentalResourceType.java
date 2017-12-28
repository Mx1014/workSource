// @formatter:off
package com.everhomes.rest.rentalv2;

/**
 * 
 * <ul>
 * <li>VIP_PARKING: vip_parking</li>
 * </ul>
 */
public enum RentalResourceType {
	VIP_PARKING("vip_parking");

	private String code;

	private RentalResourceType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static RentalResourceType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (RentalResourceType priceRuleType : RentalResourceType.values()) {
				if (priceRuleType.code.equals(code)) {
					return priceRuleType;
				}
			}
		}
		return null;
	}
}
