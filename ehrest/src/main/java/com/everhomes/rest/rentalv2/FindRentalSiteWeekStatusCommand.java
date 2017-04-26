package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>某日某场所预定状态
 * <li>siteId：场所id</li>
 * <li>ruleDate：日期</li> 
 * </ul>
 */
public class FindRentalSiteWeekStatusCommand {
	  
	@NotNull
	private Long siteId;
	@NotNull
	private Long ruleDate;
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 

	   



	public Long getRuleDate() {
		return ruleDate;
	}



	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}

 
 

	public Long getSiteId() {
		return siteId;
	}


	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
 
}
