package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>ownerType：所有者类型，参考{@link com.everhomes.rest.techpark.rental.RentalOwnerType}}</li>
 * <li>ownerId：园区id</li>
 * <li>rentalType： time(0),halfday(1)，参考{@link com.everhomes.rest.techpark.rental.RentalType} </li> 
 * <li>ruleDate：日期</li> 
 * </ul>
 */
public class FindRentalSitesStatusCommand {

	private Long communityId;

	private String ownerType; 
	private Long ownerId;
	@NotNull
	private String siteType;
	@NotNull
	private Byte rentalType;
	@NotNull
	private Long ruleDate;
	
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



	public Byte getRentalType() {
		return rentalType;
	}



	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
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


	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
 
}
