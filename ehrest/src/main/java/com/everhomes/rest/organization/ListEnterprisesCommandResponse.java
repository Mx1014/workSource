package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterprisesCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationDetailDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<OrganizationDetailDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<OrganizationDetailDTO> dtos) {
		this.dtos = dtos;
	}

    
}
