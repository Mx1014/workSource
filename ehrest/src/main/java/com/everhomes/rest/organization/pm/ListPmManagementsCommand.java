package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>organizationId: 机构id</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListPmManagementsCommand {

	@NotNull
	private Long communityId;
	
	@NotNull
	private Long organizationId;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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
}
