package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>ruleDate：日期</li> 
 * </ul>
 */

public class FindRentalSiteRulesCommand {
	private Long enterpriseCommunityId;
	private String siteType;

	private Long rentalSiteId;
	private String ruleDate;

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

	public String getRuleDate() {
		return ruleDate;
	}

	public void setRuleDate(String ruleDate) {
		this.ruleDate = ruleDate;
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
}
