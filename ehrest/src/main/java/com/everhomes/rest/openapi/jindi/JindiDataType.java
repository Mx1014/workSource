// @formatter:off
package com.everhomes.rest.openapi.jindi;

/**
 * 
 * <ul>金地同步数据的数据类型
 * <li>CUSTOMER: customer，客户</li>
 * <li>CSTHOMEREL: csthomerel，客房</li>
 * <li>ACTION: action，行为</li>
 * </ul>
 */
public enum JindiDataType {
	USER(JindiDataTypeCode.USER_CODE), 
	ORGANIZATION(JindiDataTypeCode.ORGANIZATION_CODE), 
	CSTHOMEREL(JindiDataTypeCode.CSTHOMEREL_CODE), 
	ACTION(JindiDataTypeCode.ACTION_CODE);
	
	public static class JindiDataTypeCode{
		public static final String USER_CODE = "user";
		public static final String ORGANIZATION_CODE = "organization";
		public static final String CSTHOMEREL_CODE = "csthomerel";
		public static final String ACTION_CODE = "action";
	}
	
	private String code;
	
	private JindiDataType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public static JindiDataType fromCode(String code) {
		if (code != null) {
			for (JindiDataType jindiDataType : JindiDataType.values()) {
				if (jindiDataType.getCode().equals(code)) {
					return jindiDataType;
				}
			}
		}
		return null;
	}
}
