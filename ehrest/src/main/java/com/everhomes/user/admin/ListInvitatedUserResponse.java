package com.everhomes.user.admin;

import java.util.List;

public class ListInvitatedUserResponse {

	private List<InvitatedUsers> invitatedUsers;
	
	private Long nextAnchor;

	public List<InvitatedUsers> getInvitatedUsers() {
		return invitatedUsers;
	}

	public void setInvitatedUsers(List<InvitatedUsers> invitatedUsers) {
		this.invitatedUsers = invitatedUsers;
	}

	public Long getNextAnchor() {
		return nextAnchor;
	}

	public void setNextAnchor(Long nextAnchor) {
		this.nextAnchor = nextAnchor;
	}
	
	
}
