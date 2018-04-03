package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkGetSimpleQRCode {
	private String auth;
	private String md5;
	private String phone;
	private String type;
	private AclinkSimpleQRCodeInvitation invitation;

    public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AclinkSimpleQRCodeInvitation getInvitation() {
		return invitation;
	}

	public void setInvitation(AclinkSimpleQRCodeInvitation invitation) {
		this.invitation = invitation;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
