package com.everhomes.rest.community;

import com.everhomes.discover.ItemType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>projectId: 项目id</li>
 * <li>projectType: 项目类型 </li>
 * <li>name: 子项目名称 </li>
 * <li>buildingIds: 楼栋集 </li>
 * </ul>
 */
public class CreateChildProjectCommand {
	
	@NotNull
	private Long projectId;

	@NotNull
	private String projectType;

	@NotNull
	private String name;

	@ItemType(Long.class)
	private List<Long> buildingIds;


	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getBuildingIds() {
		return buildingIds;
	}

	public void setBuildingIds(List<Long> buildingIds) {
		this.buildingIds = buildingIds;
	}
}
