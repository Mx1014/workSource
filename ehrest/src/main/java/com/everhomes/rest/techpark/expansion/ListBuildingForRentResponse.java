package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListBuildingForRentResponse {
    private Long nextPageAnchor;
    
    @ItemType(BuildingForRentDTO.class)
    private List<BuildingForRentDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<BuildingForRentDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<BuildingForRentDTO> dtos) {
		this.dtos = dtos;
	}

}
