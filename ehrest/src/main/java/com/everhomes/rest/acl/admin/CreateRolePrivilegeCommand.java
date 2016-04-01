package com.everhomes.rest.acl.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>roleName:角色名称</li>
 * <li>privilegeIds:权限集合</li>
 * </ul>
 */
public class CreateRolePrivilegeCommand {
	
	private Long organizationId;
	
    private String roleName;
    
    private String description;
    
    @ItemType(Long.class)
    private List<Long> privilegeIds;
    
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
