package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountId: 账号id</li>
 *  <li>validDate: 有效期</li>
 * </ul>
 *
 */
public class ExtendedVideoConfAccountPeriodCommand {
	
	private Long accountId;
	
	private Long validDate;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
