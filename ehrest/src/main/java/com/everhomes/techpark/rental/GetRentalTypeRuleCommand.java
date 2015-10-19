package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

/**
 * <ul>
*获取预定规则 
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>  * </ul>
 */
public class GetRentalTypeRuleCommand {
	private Long enterpriseCommunityId;
	private String siteType;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}
	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}

}
