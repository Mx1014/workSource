package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListRentalBillCountCommand {

	private String ownerType;
	private Long ownerId; 
	private String siteType;
	private Long rentalSiteId;
	private Long beginDate;
	private Long endDate;
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
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


	public String getSiteType() {
		return siteType;
	}


	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

 
	public Long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}


	public Long getRentalSiteId() {
		return rentalSiteId;
	}


	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}
	
}
