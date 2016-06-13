package com.everhomes.rest.user.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

public class UsersWithAddrResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(ListUsersWithAddrResponse.class)
	private List<ListUsersWithAddrResponse> users;

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<ListUsersWithAddrResponse> getUsers() {
		return users;
	}

	public void setUsers(List<ListUsersWithAddrResponse> users) {
		this.users = users;
	}
	
}
