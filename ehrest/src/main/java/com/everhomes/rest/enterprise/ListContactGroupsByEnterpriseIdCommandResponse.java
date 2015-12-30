package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListContactGroupsByEnterpriseIdCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseContactDTO.class)
    private List<EnterpriseContactGroupDTO> contactGroups;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<EnterpriseContactGroupDTO> getContactGroups() {
		return contactGroups;
	}

	public void setContactGroups(List<EnterpriseContactGroupDTO> contactGroups) {
		this.contactGroups = contactGroups;
	} 
    
    
}
