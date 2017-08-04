// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>EXPRESS_LETTER : 1, 包装箱</li>
 * <li>PACKING_BOX : 2, 快递袋</li>
 * <li>EXPRESS_BAG : 3, 快递封</li>
 * <li>SIX_YUAN_EXPRESS_LETTER : 4, 6元快递封</li>
 * <li>EIGHT_YUAN_EXPRESS_LETTER : 5, 8元快递封</li>
 * </ul>
 */
public enum ExpressPackageType {
	EXPRESS_LETTER((byte)1,"包装箱",null),
	PACKING_BOX((byte)2,"快递袋",null),
	EXPRESS_BAG((byte)3,"快递封",null),
	SIX_YUAN_EXPRESS_LETTER((byte)4,"6元快递封", "6"),
	EIGHT_YUAN_EXPRESS_LETTER((byte)5,"8元快递封", "8");
	
	private byte code;
	private String description;
	private String price;
	
	private ExpressPackageType(byte code, String description, String price) {
		this.code = code;
		this.description = description;
		this.price = price;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getPrice() {
		return price;
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
