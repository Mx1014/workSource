package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkHuarunVerifyUser {
	String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
