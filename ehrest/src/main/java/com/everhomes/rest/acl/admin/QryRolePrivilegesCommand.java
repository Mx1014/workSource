package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>roleId: 角色id</li>
 * </ul>
 */
public class QryRolePrivilegesCommand {
	
	private Long roleId;
	

	public Long getRoleId() {
		return roleId;
	}


	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
