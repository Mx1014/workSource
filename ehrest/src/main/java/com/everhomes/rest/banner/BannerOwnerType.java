// @formatter:off
package com.everhomes.rest.banner;

/**
 * <ul>
 * <li>ORGANIZATION("organization"):公司</li>
 * </ul>
 */
public enum BannerOwnerType {
	
	ORGANIZATION("organization");

	private String code;

	private BannerOwnerType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public static BannerOwnerType fromCode(String code) {
		if(code != null) {
			for(BannerOwnerType ownerType : BannerOwnerType.values()) {
				if(ownerType.getCode().equals(code)) {
					return ownerType;
				}
			}
		}
		return null;
	}
}
