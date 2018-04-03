package com.everhomes.rest.community;

import com.everhomes.rest.address.CommunityDTO;

import java.util.List;

/**
 * <ul>
 * <li>resourceType 资源类型  </li>
 * <li>resourceId: 资源实体id</li>
 * <li>resourceName: 资源名称</li>
 * </ul>
 */
public class ResourceCategoryAssignmentDTO {

	private String resourceType;

	private Long resourceId;

	private String resourceName;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
}
