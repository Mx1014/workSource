package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


public class OrganizationTreeDTO {

	private Long organizationId;

	private String organizationName;

	private Long parentId;

	private String path;

	private String groupType;

	@ItemType(OrganizationTreeDTO.class)
	private List<OrganizationTreeDTO> trees;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public List<OrganizationTreeDTO> getTrees() {
		return trees;
	}

	public void setTrees(List<OrganizationTreeDTO> trees) {
		this.trees = trees;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
