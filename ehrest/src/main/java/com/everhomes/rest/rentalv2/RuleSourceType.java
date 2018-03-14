// @formatter:off
package com.everhomes.rest.rentalv2;

/**
 * 
 * <ul>
 * <li>DEFAULT: default_rule</li>
 * <li>RESOURCE: resource_rule</li>
 * </ul>
 */
public enum RuleSourceType {
	DEFAULT("default_rule"), RESOURCE("resource_rule");

	private String code;

	private RuleSourceType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static RuleSourceType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (RuleSourceType priceRuleType : RuleSourceType.values()) {
				if (priceRuleType.code.equals(code)) {
					return priceRuleType;
				}
			}
		}
		return null;
	}
}
