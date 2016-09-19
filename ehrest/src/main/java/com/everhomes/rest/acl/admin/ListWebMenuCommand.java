package com.everhomes.rest.acl.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>namespaceId: 域id</li>
 * </ul>
 */
public class ListWebMenuCommand {
	
	@NotNull
	private Long organizationId;

	private Integer namespaceId;
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
