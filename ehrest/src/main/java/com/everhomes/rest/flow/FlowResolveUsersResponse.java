package com.everhomes.rest.flow;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.StringHelper;

public class FlowResolveUsersResponse {
	@ItemType(UserInfo.class)
	private List<UserInfo> users;

	public List<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
