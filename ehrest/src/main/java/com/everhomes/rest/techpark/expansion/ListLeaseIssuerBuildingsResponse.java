package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>buildings: 楼栋信息，参考{@link BuildingDTO}</li>
 * </ul>
 */
public class ListLeaseIssuerBuildingsResponse {

	private Long nextPageAnchor;
	
	@ItemType(BuildingDTO.class)
	private List<BuildingDTO> buildings;
	
	public ListLeaseIssuerBuildingsResponse() {
    }
    
    public ListLeaseIssuerBuildingsResponse(Long nextPageAnchor, List<BuildingDTO> buildings) {
        this.nextPageAnchor = nextPageAnchor;
        this.buildings = buildings;
    }

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
	
	 @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
