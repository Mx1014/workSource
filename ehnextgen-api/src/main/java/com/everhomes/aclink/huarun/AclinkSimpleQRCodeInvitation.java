package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkSimpleQRCodeInvitation {
	private String invitee;
	private String date;
	private String invitee_tel;

    public String getInvitee() {
		return invitee;
	}

	public void setInvitee(String invitee) {
		this.invitee = invitee;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInvitee_tel() {
		return invitee_tel;
	}

	public void setInvitee_tel(String invitee_tel) {
		this.invitee_tel = invitee_tel;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
