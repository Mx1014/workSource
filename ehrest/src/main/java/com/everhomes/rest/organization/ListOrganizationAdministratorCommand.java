// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId：组织类型</li>
 * <li>keywords：关键词</li>
 * <li>roleIds：角色Id列表</li>
 * </ul>
 */
public class ListOrganizationAdministratorCommand {
	
	private Long organizationId;

	private String keywords;

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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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
