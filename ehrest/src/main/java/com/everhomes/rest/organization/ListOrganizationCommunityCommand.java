// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListOrganizationCommunityCommand {
	@NotNull
	private Long    organizationId;
	private Integer pageOffset;
	private Integer pageSize;
	
	public ListOrganizationCommunityCommand() {
    }

	


	public Long getOrganizationId() {
		return organizationId;
	}




	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}




	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
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
