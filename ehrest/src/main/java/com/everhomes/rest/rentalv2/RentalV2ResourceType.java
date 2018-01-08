// @formatter:off
package com.everhomes.rest.rentalv2;

/**
 * 
 * <ul>
 * <li>DEFAULT: default, 以前的资源预约都默认default</li>
 * <li>VIP_PARKING: vip_parking</li>
 * </ul>
 */
public enum RentalResourceType {
	DEFAULT("default"), VIP_PARKING("vip_parking");

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
