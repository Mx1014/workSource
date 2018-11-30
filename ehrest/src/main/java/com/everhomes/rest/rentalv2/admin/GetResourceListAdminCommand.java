package com.everhomes.rest.rentalv2.admin;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * 列出资源列表参数(根据图标和园区)
 * <li>ownerId: 可见id</li>
 * <li>ownerType: 可见范围类型</li>
 * <li>communityId: 所属园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>organizationId: 所属公司id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize:每页数量 </li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class GetResourceListAdminCommand {
	private Long ownerId;
	private String ownerType;

	private Long communityId;
	private String resourceType;
	@NotNull
	private Long resourceTypeId;
	private Long organizationId;
	private Byte status;

    
	private Long pageAnchor;
    
	private Integer pageSize;

	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

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

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}