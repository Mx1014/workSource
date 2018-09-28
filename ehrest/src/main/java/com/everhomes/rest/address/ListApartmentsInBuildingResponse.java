package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListApartmentsInBuildingResponse {
	
	 private Long nextPageAnchor;
    @ItemType(ApartmentBriefInfoDTO.class)
    private List<ApartmentBriefInfoDTO> apartments;
    
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<ApartmentBriefInfoDTO> getApartments() {
		return apartments;
	}
	public void setApartments(List<ApartmentBriefInfoDTO> apartments) {
		this.apartments = apartments;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
