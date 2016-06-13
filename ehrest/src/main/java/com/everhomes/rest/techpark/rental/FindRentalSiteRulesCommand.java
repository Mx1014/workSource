package com.everhomes.rest.techpark.rental;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>ruleDate：日期</li> 
 * <li>rentalSiteId：场所id</li> 
 * <li>rentalType： time(0),halfday(1){@link com.everhomes.rest.techpark.rental.RentalType} </li> 
 * </ul>
 */

public class FindRentalSiteRulesCommand {
	@NotNull
	private Long enterpriseCommunityId;
	@NotNull
	private String siteType;
	@NotNull
	private Long rentalSiteId;
	@NotNull
	private Byte rentalType;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
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

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}
}
