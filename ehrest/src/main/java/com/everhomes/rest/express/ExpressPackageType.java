// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>EXPRESS_LETTER : 1, 快递封</li>
 * <li>PACKING_BOX : 2, 包装箱</li>
 * <li>EXPRESS_BAG : 3, 快递袋</li>
 * <li>SIX_YUAN_EXPRESS_LETTER : 6, 6元快递封</li>
 * <li>EIGHT_YUAN_EXPRESS_LETTER : 8, 8元快递封</li>
 * </ul>
 */
public enum ExpressPackageType {
	EXPRESS_LETTER((byte)1),
	PACKING_BOX((byte)2),
	EXPRESS_BAG((byte)3),
	SIX_YUAN_EXPRESS_LETTER((byte)6),
	EIGHT_YUAN_EXPRESS_LETTER((byte)8);
	
	private Byte code;

	private ExpressPackageType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static ExpressPackageType fromCode(Byte code) {
		if (code != null) {
			for (ExpressPackageType expressPackageType : ExpressPackageType.values()) {
				if (expressPackageType.getCode().byteValue() == code.byteValue()) {
					return expressPackageType;
				}
			}
		}
		return null;
	}
}
