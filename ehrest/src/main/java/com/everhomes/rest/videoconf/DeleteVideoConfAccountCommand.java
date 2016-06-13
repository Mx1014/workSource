package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * accountId: 账号id
 *
 */
public class DeleteVideoConfAccountCommand {

	private Long accountId;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
