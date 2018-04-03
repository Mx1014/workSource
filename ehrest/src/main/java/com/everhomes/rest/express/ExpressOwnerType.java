// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>COMMUNITY: community，园区</li>
 * </ul>
 */
public enum ExpressOwnerType {
	COMMUNITY("community");
	
	private String code;

	private ExpressOwnerType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static ExpressOwnerType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (ExpressOwnerType expressOwnerType : ExpressOwnerType.values()) {
				if (expressOwnerType.getCode().equals(code)) {
					return expressOwnerType;
				}
			}
		}
		return null;
	}
}
