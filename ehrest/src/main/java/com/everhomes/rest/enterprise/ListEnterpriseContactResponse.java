package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterpriseContactResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseContactDTO.class)
    private List<EnterpriseContactDTO> contacts;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnterpriseContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<EnterpriseContactDTO> contacts) {
        this.contacts = contacts;
    }
    
    
}
