package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

public class EnterpriseCommunityResponse {
    private Long nextPageAnchor;
    
    @ItemType(EnterpriseCommunityDTO.class)
    private List<EnterpriseCommunityDTO> communities;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<EnterpriseCommunityDTO> getCommunities() {
        return communities;
    }

    public void setCommunities(List<EnterpriseCommunityDTO> communities) {
        this.communities = communities;
    }
    
    
}
