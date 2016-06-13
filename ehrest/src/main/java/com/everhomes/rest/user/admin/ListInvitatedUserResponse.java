package com.everhomes.rest.user.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListInvitatedUserResponse {
    @ItemType(InvitatedUsers.class)
	private List<InvitatedUsers> invitatedUsers;
	
	private Long nextPageAnchor;

	public List<InvitatedUsers> getInvitatedUsers() {
		return invitatedUsers;
	}

	public void setInvitatedUsers(List<InvitatedUsers> invitatedUsers) {
		this.invitatedUsers = invitatedUsers;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

}
