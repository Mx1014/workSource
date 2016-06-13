package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accounts: 试用账号数</li>
 *  <li>months: 试用时间</li>
 * </ul>
 */
public class SetVideoConfAccountTrialRuleCommand {

	private String accounts;
	
	private String months;


	public String getAccounts() {
		return accounts;
	}


	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}


	public String getMonths() {
		return months;
	}


	public void setMonths(String months) {
		this.months = months;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
