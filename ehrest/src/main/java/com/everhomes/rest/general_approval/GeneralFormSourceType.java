package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>LEASE_PROMOTION: 招租管理</li>
 * <li>ZJ_PERSONAL_AUTH("zj_personal_auth"): 个人认证表单</li>
 * <li>ZJ_ORGANIZATION_AUTH("zj_organization_auth"):  公司认证表单</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormSourceType {
	LEASE_PROMOTION("EhLeasePromotions"), GENERAL_APPROVE("GENERAL_APPROVE"), BUILDING("EhBuildings"),
	ZJ_PERSONAL_AUTH("zj_personal_auth"),ZJ_ORGANIZATION_AUTH("zj_organization_auth");

	private String code;

	private GeneralFormSourceType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static GeneralFormSourceType fromCode(String code) {
		for(GeneralFormSourceType v : GeneralFormSourceType.values()) {
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
