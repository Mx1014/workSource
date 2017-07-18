// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>EXPRESS_LETTER : express_letter 快递封</li>
 * <li>PACKING_BOX : packing_box 包装袋</li>
 * <li>EXPRESS_BAG : express_bag 快递袋</li>
 * <li>SIX_YUAN_EXPRESS_LETTER : six_yuan_express_letter 6元快递封</li>
 * <li>EIGHT_YUAN_EXPRESS_LETTER : eight_yuan_express_letter 8元快递封</li>
 * </ul>
 */
public enum ExpressPackageType {
	EXPRESS_LETTER("express_letter"),
	PACKING_BOX("packing_box"),
	EXPRESS_BAG("express_bag"),
	SIX_YUAN_EXPRESS_LETTER("six_yuan_express_letter"),
	EIGHT_YUAN_EXPRESS_LETTER("eight_yuan_express_letter");
	
	private String code;

	private ExpressPackageType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static ExpressPackageType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (ExpressPackageType expressPackageType : ExpressPackageType.values()) {
				if (expressPackageType.getCode().equals(code)) {
					return expressPackageType;
				}
			}
		}
		return null;
	}
}
