package com.everhomes.rest.user.admin;

import java.sql.Timestamp;

public class InvitatedUsers {
	
	private Long userId;
	
	private Long inviterId;
	
	private String userNickName;
	
	private String userCellPhone;
	
	private Timestamp registerTime;
	
	private int inviteType;
	
	private String inviter;
	
	private String inviterCellPhone;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getInviterId() {
		return inviterId;
	}

	public void setInviterId(Long inviterId) {
		this.inviterId = inviterId;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserCellPhone() {
		return userCellPhone;
	}

	public void setUserCellPhone(String userCellPhone) {
		this.userCellPhone = userCellPhone;
	}

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public int getInviteType() {
		return inviteType;
	}

	public void setInviteType(int inviteType) {
		this.inviteType = inviteType;
	}

	public String getInviter() {
		return inviter;
	}

	public void setInviter(String inviter) {
		this.inviter = inviter;
	}

	public String getInviterCellPhone() {
		return inviterCellPhone;
	}

	public void setInviterCellPhone(String inviterCellPhone) {
		this.inviterCellPhone = inviterCellPhone;
	}
}
