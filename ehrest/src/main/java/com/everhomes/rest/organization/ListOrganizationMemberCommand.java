// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
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
	
	@ItemType(String.class)
	private List<String> groupTypes;
	
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


	public List<String> getGroupTypes() {
		return groupTypes;
	}


	public void setGroupTypes(List<String> groupTypes) {
		this.groupTypes = groupTypes;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
