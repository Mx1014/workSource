package com.everhomes.rest.relocation;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/11/20.
 */
public class SearchRelocationRequestsResponse {

    private Long nextPageAnchor;

    @ItemType(RelocationRequestDTO.class)
    private List<RelocationRequestDTO> requests;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<RelocationRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<RelocationRequestDTO> requests) {
        this.requests = requests;
    }
}
