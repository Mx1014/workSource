package com.everhomes.rest.techpark.rental.admin;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * 列出资源列表参数(根据图标和园区)
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者类型</li>
 * <li>launchPadItemId: 图标id</li>
 * <li>organizationId: 所属公司id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize:每页数量 </li>
 * </ul>
 */
public class GetResourceListAdminCommand {
	private Long ownerId;
	private String ownerType;
	@NotNull
	private Long launchPadItemId;
	private Long organizationId;

    
	private Long pageAnchor;
    
	private Integer pageSize;
	
	
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getLaunchPadItemId() {
		return launchPadItemId;
	}

	public void setLaunchPadItemId(Long launchPadItemId) {
		this.launchPadItemId = launchPadItemId;
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