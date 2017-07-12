package com.everhomes.aclink.huarun;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AclinkHuarunVerifyUserResp {
	private Long status;
	
	@ItemType(AclinkHuarunUser.class)
	private List<AclinkHuarunUser> user;

    public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public List<AclinkHuarunUser> getUser() {
		return user;
	}

	public void setUser(List<AclinkHuarunUser> user) {
		this.user = user;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
