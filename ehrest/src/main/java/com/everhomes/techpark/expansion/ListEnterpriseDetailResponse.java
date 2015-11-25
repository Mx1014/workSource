package com.everhomes.techpark.expansion;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterpriseDetailResponse {
    private Integer nextPageAnchor;
    
    @ItemType(EnterpriseDetailDTO.class)
    private List<EnterpriseDetailDTO> details;

    public Integer getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Integer nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<EnterpriseDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<EnterpriseDetailDTO> details) {
		this.details = details;
	}

	
}
