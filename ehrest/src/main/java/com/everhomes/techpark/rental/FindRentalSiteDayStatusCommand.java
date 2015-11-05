package com.everhomes.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>rentalSiteId：场所id</li>
 * <li>rentalType： time(0),halfday(1){@link com.everhomes.techpark.rental.RentalType} </li> 
 * <li>ruleDate：日期</li> 
 * </ul>
 */
public class FindRentalSiteDayStatusCommand {
	@NotNull
	private Long communityId;
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


	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
}
