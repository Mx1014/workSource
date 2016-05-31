package com.everhomes.rest.search;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;

public class OrganizationQueryResult {
    private Long pageAnchor;
    
    @ItemType(Long.class)
    private List<Long> ids;
    
    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> dtos;
   
    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public List<Long> getIds() {
        return ids;
    }
    
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
    
    public List<OrganizationDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<OrganizationDTO> dtos) {
		this.dtos = dtos;
	}
}
