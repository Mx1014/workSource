package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListPmBuildingCommandResponse {
	
	@ItemType(PmBuildingDTO.class)
	private List<PmBuildingDTO> pmBuildings;
	
	private Long nextPageOffset;

	public List<PmBuildingDTO> getPmBuildings() {
		return pmBuildings;
	}

	public void setPmBuildings(List<PmBuildingDTO> pmBuildings) {
		this.pmBuildings = pmBuildings;
	}

	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

}
