package com.everhomes.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListBuildingForRentResponse {
    private Integer nextPageAnchor;
    
    @ItemType(BuildingForRentDTO.class)
    private List<BuildingForRentDTO> dtos;

    public Integer getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Integer nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<BuildingForRentDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<BuildingForRentDTO> dtos) {
		this.dtos = dtos;
	}

}
