// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>APP: app，app支付</li>
 * <li>OFFICIAL_ACCOUNTS: official_accounts，公众号支付</li>
 * </ul>
 */
public enum ExpressClientPayType {
	APP("app"), OFFICIAL_ACCOUNTS("official_accounts");
	
	private String code;

	private ExpressClientPayType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static ExpressClientPayType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (ExpressClientPayType expressOwnerType : ExpressClientPayType.values()) {
				if (expressOwnerType.getCode().equals(code)) {
					return expressOwnerType;
				}
			}
		}
		return null;
	}
}
