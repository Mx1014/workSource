package com.everhomes.enterprise;

import com.everhomes.discover.ItemType;

public class ListEnterpriseResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseDTO.class)
    private EnterpriseDTO enterprises;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public EnterpriseDTO getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(EnterpriseDTO enterprises) {
        this.enterprises = enterprises;
    }
    
    
}
