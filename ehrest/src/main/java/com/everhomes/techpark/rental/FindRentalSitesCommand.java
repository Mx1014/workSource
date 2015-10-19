package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 查询场所
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * </ul>
 */
public class FindRentalSitesCommand {
	private Long enterpriseCommunityId;
	private String siteType;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}
	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
}
