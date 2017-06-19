package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkHuarunSyncUser {
	private String auth;
	private String md5;
	private String phone;
	private String username;
	private String organization;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
