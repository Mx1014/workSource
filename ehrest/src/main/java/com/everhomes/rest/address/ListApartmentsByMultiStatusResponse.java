package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListApartmentsByMultiStatusResponse {
	
	private Long nextPageAnchor;
    @ItemType(ApartmentBriefInfoDTO.class)
    private List<ApartmentBriefInfoDTO> apartments;

    public List<ApartmentBriefInfoDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentBriefInfoDTO> apartments) {
        this.apartments = apartments;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
}
