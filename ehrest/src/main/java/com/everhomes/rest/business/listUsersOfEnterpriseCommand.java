package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

public class listUsersOfEnterpriseCommand {
	private Long OrganizationId;

	private Long pageAnchor;
	private Integer pageSize;

	public Long getOrganizationId() {
		return OrganizationId;
	}

	public void setOrganizationId(Long organizationId) {
		OrganizationId = organizationId;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
