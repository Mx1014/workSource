// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：组织类型</li>
 * <li>roleIds：角色Id列表</li>
 * </ul>
 */
public class ListOrganizationAdministratorCommand {
	
	private Long organizationId;
	
	@ItemType(Long.class)
	private List<Long> roleIds;
	
	public ListOrganizationAdministratorCommand() {
    }

	
	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public List<Long> getRoleIds() {
		return roleIds;
	}


	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
