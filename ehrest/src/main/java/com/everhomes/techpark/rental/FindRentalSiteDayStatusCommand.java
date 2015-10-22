package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>rentalSiteId：场所id</li>
 * <li>ruleDate：日期</li> 
 * </ul>
 */
public class FindRentalSiteDayStatusCommand {
	private Long enterpriseCommunityId;
	private String siteType; 
	private Long ruleDate;
	
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



	public Long getRuleDate() {
		return ruleDate;
	}



	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}
}
