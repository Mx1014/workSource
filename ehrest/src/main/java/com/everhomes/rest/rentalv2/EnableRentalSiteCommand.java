package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 更新预定规则
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>rentalSiteId：场所id</li> 
 * </ul>
 */
public class EnableRentalSiteCommand {
	@NotNull
	private String ownerType;
	private Long ownerId;
	@NotNull
	private String siteType;
	@NotNull
	private Long rentalSiteId; 

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
 
	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
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
