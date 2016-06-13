package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accounts: 多账号起售数量</li>
 * </ul>
 *
 */
public class SetMinimumAccountsCommand {
	
	private String accounts;

	public String getAccounts() {
		return accounts;
	}

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
