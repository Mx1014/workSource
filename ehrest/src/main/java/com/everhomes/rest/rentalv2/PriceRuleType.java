// @formatter:off
package com.everhomes.rest.rentalv2;

/**
 * 
 * <ul>
 * <li>DEFAULT: default</li>
 * <li>RESOURCE: resource</li>
 * <li>CELL: cell</li>
 * </ul>
 */
public enum PriceRuleType {
	DEFAULT("default"), RESOURCE("resource"), CELL("cell");
	
	private String code;
	
	private PriceRuleType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static PriceRuleType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (PriceRuleType priceRuleType : PriceRuleType.values()) {
				if (priceRuleType.code.equals(code)) {
					return priceRuleType;
				}
			}
		}
		return null;
	}
}
