package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id，用于权限校验</li>
 * <li>organizationId: 管理公司id，用于权限校验</li>
 * <li>communityId: 园区id，用于权限校验</li>
 * </ul>
 */
public class ChangeBuildingOrderCommand {
	
	private Long communityId;
    private Integer namespaceId;
    private Long organizationId;
	private List<BuildingOrderDTO> buildingOrders;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<BuildingOrderDTO> getBuildingOrders() {
		return buildingOrders;
	}

	public void setBuildingOrders(List<BuildingOrderDTO> buildingOrders) {
		this.buildingOrders = buildingOrders;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
