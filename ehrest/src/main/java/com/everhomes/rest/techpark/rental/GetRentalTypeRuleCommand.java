package com.everhomes.rest.techpark.rental;

import com.everhomes.util.StringHelper;

/**
 * <ul>获取预定规则 
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * </ul>
 */
public class GetRentalTypeRuleCommand {

	private String ownerType;
	private Long ownerId;
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
	 
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

}
