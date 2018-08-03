package com.everhomes.rest.community;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>nextPageAnchor: 下页的锚点</li>
 *     <li>pageSize: 每页显示的数量</li>
 *     <li>buildings: 楼栋集合 {@link com.everhomes.rest.community.BuildingInfoDTO}</li>
 * </ul>
 */
public class ListBuildingsByKeywordsResponse {
	
	private Long nextPageAnchor;
	
	private Integer pageSize;
	
	@ItemType(BuildingInfoDTO.class)
	private List<BuildingInfoDTO> buildings;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<BuildingInfoDTO> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingInfoDTO> buildings) {
		this.buildings = buildings;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
