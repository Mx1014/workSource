package com.everhomes.rest.community;

import com.everhomes.discover.ItemType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>id: 子项目项目id</li>
 * <li>name: 子项目名称 </li>
 * <li>buildingIds: 楼栋集 </li>
 * </ul>
 */
public class UpdateChildProjectCommand {

	@NotNull
	private Long id;

	@NotNull
	private String name;

	@ItemType(Long.class)
	private List<Long> buildingIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
