// @formatter:off
package com.everhomes.rest.rentalv2;

/**
 * 
 * <ul>
 * <li>DEFAULT: default, 以前的资源预约都默认default</li>
 * <li>VIP_PARKING: vip_parking</li>
 * <li>STATION_BOOKING: 工位预定</li>
 * </ul>
 */
public enum RentalV2ResourceType {
	DEFAULT("default"), VIP_PARKING("vip_parking"),CONFERENCE("conference"),SCREEN("screen"),AREA("area"),STATION_BOOKING("station_booking");

	private String code;

	private RentalV2ResourceType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static RentalV2ResourceType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (RentalV2ResourceType priceRuleType : RentalV2ResourceType.values()) {
				if (priceRuleType.code.equals(code)) {
					return priceRuleType;
				}
			}
		}
		return null;
	}
}
