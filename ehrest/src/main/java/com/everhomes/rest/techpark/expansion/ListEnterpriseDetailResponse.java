package com.everhomes.rest.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterpriseDetailResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseDetailDTO.class)
    private List<EnterpriseDetailDTO> details;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<EnterpriseDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<EnterpriseDetailDTO> details) {
		this.details = details;
	}

	
}
