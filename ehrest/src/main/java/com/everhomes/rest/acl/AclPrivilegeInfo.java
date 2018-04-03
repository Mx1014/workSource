package com.everhomes.rest.acl;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AclPrivilegeInfo {
	private Long privilegeId;
	private Long roleId;
	
	@ItemType(ServiceModulePrivilegeDTO.class)
	List<ServiceModulePrivilegeDTO> modulePrivileges;

	public Long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<ServiceModulePrivilegeDTO> getModulePrivileges() {
		return modulePrivileges;
	}

	public void setModulePrivileges(List<ServiceModulePrivilegeDTO> modulePrivileges) {
		this.modulePrivileges = modulePrivileges;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
