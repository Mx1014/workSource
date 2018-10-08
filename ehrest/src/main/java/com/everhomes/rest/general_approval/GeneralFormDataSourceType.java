package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>USER_NAME: 当前用户名  </li>
 * <li>USER_PHONE: 当前用户手机号</li>
 * <li>USER_COMPANY: 用户所在公司</li>
 * <li>SOURCE_ID: 来源id</li>
 * <li>USER_ADDRESS: 楼栋门牌</li>
 * <li>ORGANIZATION_ID: 公司id</li>
 * <li>CUSTOM_DATA: 自定义data，定义成一个json串，可以传对接业务额外的一些数据，比如园区入驻提交表单需要传来源类型 {"sourceType": "for_rent"}</li>
 * <li>LEASE_PROMOTION_BUILDING: 楼栋名称（园区入驻专用）</li>
 * <li>LEASE_PROMOTION_APARTMENT: 门牌号（园区入驻专用）</li>
 * <li>LEASE_PROMOTION_DESCRIPTION: 备注（园区入驻专用）</li>
 * <li>ACTIVITY_ID: 活动ID</li>
 * </ul>
 * @author janson
 *
 */
public enum GeneralFormDataSourceType {
	USER_NAME("USER_NAME"), USER_PHONE("USER_PHONE"), USER_COMPANY("USER_COMPANY"), SOURCE_ID("SOURCE_ID"),
	ORGANIZATION_ID("ORGANIZATION_ID"), USER_ADDRESS("USER_ADDRESS"), CUSTOM_DATA("CUSTOM_DATA"),CUSTOMER_NAME("CUSTOMER_NAME"),
	/*----- 园区入驻 ----*/
	LEASE_PROMOTION_BUILDING("LEASE_PROMOTION_BUILDING"), LEASE_PROMOTION_APARTMENT("LEASE_PROMOTION_APARTMENT"),
	LEASE_PROMOTION_DESCRIPTION("LEASE_PROMOTION_DESCRIPTION"), LEASE_PROJECT_NAME("LEASE_PROJECT_NAME"),
	/*----- 园区入驻 ----*/
	/*-------活动ID-------*/
	ACTIVITY_ID("ACTIVITY_ID")
	;

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
