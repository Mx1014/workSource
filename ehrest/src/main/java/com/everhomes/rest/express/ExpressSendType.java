// @formatter:off
package com.everhomes.rest.express;

/**
 * 
 * <ul>
 * <li>STANDARD: 1，标准快递</li>
 * <li>EMS_STANDARD: 2，同城信筒快件</li>
 * <li>CHINA_POST_PACKAGE: 3，EMS标准快递</li>
 * <li>CITY_EMPTIES: 9，邮政快递包裹</li>
 * </ul>
 */
public enum ExpressSendType {
	STANDARD((byte)1, "标准快递"),
	CITY_EMPTIES((byte)2,"同城信筒快件"),
	EMS_STANDARD((byte)3, "EMS标准快递"),
	CHINA_POST_PACKAGE((byte)9,"邮政快递包裹");
	
	private byte code;
	private String description;
	
	private ExpressSendType(byte code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static ExpressSendType fromCode(Byte code) {
		if (code != null) {
			for (ExpressSendType expressSendType : ExpressSendType.values()) {
				if (expressSendType.getCode().byteValue() == code.byteValue()) {
					return expressSendType;
				}
			}
		}
		return null;
	} 
}
