package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkHuarunSyncUserResp {
	private Long status;
	private String description;
	
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
