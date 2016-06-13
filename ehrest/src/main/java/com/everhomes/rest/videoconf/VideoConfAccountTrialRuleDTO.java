package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accounts: 试用账号数</li>
 *  <li>months: 试用时间</li>
 * </ul>
 */
public class VideoConfAccountTrialRuleDTO {

	private Integer accounts;
	
	private Integer months;

	
	public Integer getAccounts() {
		return accounts;
	}



	public void setAccounts(Integer accounts) {
		this.accounts = accounts;
	}



	public Integer getMonths() {
		return months;
	}



	public void setMonths(Integer months) {
		this.months = months;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
