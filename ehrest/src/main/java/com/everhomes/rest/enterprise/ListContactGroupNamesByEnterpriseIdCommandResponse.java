package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListContactGroupNamesByEnterpriseIdCommandResponse {
    @ItemType(EnterpriseContactDTO.class)
    private List<EnterpriseContactGroupDTO> contactGroups;
 

	public List<EnterpriseContactGroupDTO> getContactGroups() {
		return contactGroups;
	}

	public void setContactGroups(List<EnterpriseContactGroupDTO> contactGroups) {
		this.contactGroups = contactGroups;
	} 
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    } 
    
}
