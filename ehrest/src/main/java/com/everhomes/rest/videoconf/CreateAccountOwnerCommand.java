package com.everhomes.rest.videoconf;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountIds: 账号id列表</li>
 *  <li>userIds: 用户id列表</li>
 * </ul>
 */
public class CreateAccountOwnerCommand {

	@NotNull
	@ItemType(Long.class)
	private List<Long> accountIds;
	
	@NotNull
	@ItemType(Long.class)
	private List<Long> userIds;

	public List<Long> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<Long> accountIds) {
		this.accountIds = accountIds;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
