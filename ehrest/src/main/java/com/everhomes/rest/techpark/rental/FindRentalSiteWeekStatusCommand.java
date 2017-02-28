package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>ownerType：所有者类型，参考{@link com.everhomes.rest.techpark.rental.RentalOwnerType}}</li>
 * <li>ownerId：园区id</li>
 * <li>rentalType： time(0)，halfday(1)，参考{@link com.everhomes.rest.techpark.rental.RentalType} </li> 
 * <li>ruleDate：日期</li> 
 * </ul>
 */
public class FindRentalSiteWeekStatusCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private String siteType; 
	@NotNull
	private Long siteId;
	@NotNull
	private Long ruleDate;
	private Byte rentalType;
	
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



	public Long getRuleDate() {
		return ruleDate;
	}



	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
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


	public Long getSiteId() {
		return siteId;
	}


	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}


	public Byte getRentalType() {
		return rentalType;
	}


	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}
 
}
