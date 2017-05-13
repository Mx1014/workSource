package com.everhomes.rest.acl;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AclPrivilegeInfoResponse {
	@ItemType(AclPrivilegeInfo.class)
	List<AclPrivilegeInfo> privileges;
	
	public AclPrivilegeInfoResponse() {
//		privileges = new ArrayList<AclPrivilegeInfo>();
	}

	public List<AclPrivilegeInfo> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<AclPrivilegeInfo> privileges) {
		this.privileges = privileges;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
