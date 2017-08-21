package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.RolePrivilege;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>allFlag:是否全部</li>
 * <li>privileges:具体授权信息, 参考{@link RolePrivilege}</li>
 * </ul>
 */
public class GetPrivilegeByRoleIdResponse {

	private Byte allFlag;

	@ItemType(RolePrivilege.class)
	private List<RolePrivilege> privileges;

	public Byte getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(Byte allFlag) {
		this.allFlag = allFlag;
	}

	public List<RolePrivilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<RolePrivilege> privileges) {
		this.privileges = privileges;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
