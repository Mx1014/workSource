package com.everhomes.rest.customer.openapi;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Rui.Jia  2018/7/13 17 :20
 */

public class CommunityResponse {
    private Long nextPageAnchor;

    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> requests;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<CommunityDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<CommunityDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
