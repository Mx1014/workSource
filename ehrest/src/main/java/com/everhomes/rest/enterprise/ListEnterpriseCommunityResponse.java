package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListEnterpriseCommunityResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseCommunityDTO.class)
    private List<EnterpriseCommunityDTO> enterpriseCommunities;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnterpriseCommunityDTO> getEnterpriseCommunities() {
        return enterpriseCommunities;
    }

    public void setEnterpriseCommunities(List<EnterpriseCommunityDTO> enterpriseCommunities) {
        this.enterpriseCommunities = enterpriseCommunities;
    }
    
    

}
