package com.everhomes.rest.general_approval;

import org.apache.commons.lang.StringUtils;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>USER_NAME: 当前用户名  </li>
 * <li>USER_PHONE: 当前用户手机号</li>
 * <li>USER_COMPANY: 用户所在公司</li>
 * <li>SOURCE_ID: 来源id</li>
 * <li>USER_ADDRESS: 楼栋门牌</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormDataSourceType {
 	USER_NAME("USER_NAME"), USER_PHONE("USER_PHONE"), USER_COMPANY("USER_COMPANY"), 
	SOURCE_ID("SOURCE_ID"), ORGANIZATION_ID("ORGANIZATION_ID"), USER_ADDRESS("USER_ADDRESS");
	
	private String code;
	
	private GeneralFormDataSourceType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormDataSourceType fromCode(String code) {
		for(GeneralFormDataSourceType v : GeneralFormDataSourceType.values()) {
			if(StringUtils.equals(v.getCode(), code))
				return v;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
