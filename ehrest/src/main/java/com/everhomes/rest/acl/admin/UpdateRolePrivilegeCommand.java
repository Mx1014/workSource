package com.everhomes.rest.acl.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>roleId: 角色id</li>
 * <li>roleName: 角色名称</li>
 * <li>privilegeIds: 权限集 </li>
 * </ul>
 */
public class UpdateRolePrivilegeCommand {
	
	private Long roleId;
	
    private String roleName;
    
    @ItemType(Long.class)
    private List<Long> privilegeIds;
    
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Long> getPrivilegeIds() {
		return privilegeIds;
	}

	public void setPrivilegeIds(List<Long> privilegeIds) {
		this.privilegeIds = privilegeIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
