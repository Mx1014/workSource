package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>LEASE_PROMOTION_BUILDING: 楼栋名称</li>
 * <li>LEASE_PROMOTION_APARTMENT: 门牌号</li>
 * <li>LEASE_PROMOTION_DESCRIPTION: 备注</li>
 * </ul>
 * @author janson
 *
 */
public enum LeasePromotionFormDataSourceType {
	LEASE_PROMOTION_BUILDING("LEASE_PROMOTION_BUILDING"), LEASE_PROMOTION_APARTMENT("LEASE_PROMOTION_APARTMENT"),
	LEASE_PROMOTION_DESCRIPTION("LEASE_PROMOTION_DESCRIPTION");

	private String code;

	private LeasePromotionFormDataSourceType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static LeasePromotionFormDataSourceType fromCode(String code) {
		for(LeasePromotionFormDataSourceType v : LeasePromotionFormDataSourceType.values()) {
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
