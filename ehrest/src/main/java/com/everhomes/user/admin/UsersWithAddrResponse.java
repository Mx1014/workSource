package com.everhomes.user.admin;

import java.util.List;

public class UsersWithAddrResponse {
	
	private Integer nextPageOffset;
	
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
