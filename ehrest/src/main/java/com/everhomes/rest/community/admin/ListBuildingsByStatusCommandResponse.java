package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>buildings: 申请楼栋信息</li>
 * </ul>
 */
public class ListBuildingsByStatusCommandResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(BuildingDTO.class)
	private List<BuildingDTO> buildings;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<BuildingDTO> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingDTO> buildings) {
		this.buildings = buildings;
	}

}
