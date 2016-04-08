package com.everhomes.rest.acl.admin;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>roleId: 角色id</li>
 * <li>organizationId: 机构id</li>
 * </ul>
 */
public class QryRolePrivilegesCommand {
	
	private Long roleId;
	
	private Long organizationId;
	

	public Long getRoleId() {
		return roleId;
	}


	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	
	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
