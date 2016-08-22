package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>rentalSiteId：场所id</li>
 * <li>ruleDates：日期json字符串</li> 
 * </ul>
 */
public class DeleteRentalSiteRulesCommand {
	@NotNull
	private Long enterpriseCommunityId;
	@NotNull
	private String siteType; 
	@NotNull
	private String rentalSiteId;
	@NotNull
	private String ruleDates;
	
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

 

	public String getRentalSiteId() {
		return rentalSiteId;
	}



	public void setRentalSiteId(String rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}



	public String getRuleDates() {
		return ruleDates;
	}



	public void setRuleDates(String ruleDates) {
		this.ruleDates = ruleDates;
	}
}
