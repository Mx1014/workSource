// @formatter:off
package com.everhomes.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：政府机构id</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListOrganizationMemberCommand {
	@NotNull
	private Long    organizationId;
	private Long pageAnchor;
	
	private Integer pageOffset;
	
	private Integer pageSize;
	
	public ListOrganizationMemberCommand() {
    }

	
	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	
	

	public Integer getPageOffset() {
		return pageOffset;
	}


	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
