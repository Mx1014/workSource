package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>projectType: 资源实体类型 小区园区：EhCommunities</li>
 *     <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class GetCommunityDetailCommand {
	
	private Long communityId;
	private String projectType;
	private Integer namespaceId;
	
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
