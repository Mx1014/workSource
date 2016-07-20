package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>rentalSiteId：物品id</li> 
 * </ul>
 */
public class DeleteRentalSiteItemCommand {
	@NotNull
	private Long enterpriseCommunityId;
	@NotNull
	private String siteType;
	@NotNull
	private Long rentalSiteItemId; 
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
	public Long getRentalSiteItemId() {
		return rentalSiteItemId;
	}
	public void setRentalSiteItemId(Long rentalSiteItemId) {
		this.rentalSiteItemId = rentalSiteItemId;
	}
}
