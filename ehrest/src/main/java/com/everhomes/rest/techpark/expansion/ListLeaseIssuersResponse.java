package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class ListLeaseIssuersResponse {
    private Long nextPageAnchor;
    @ItemType(LeaseIssuerDTO.class)
    private List<LeaseIssuerDTO> requests;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<LeaseIssuerDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<LeaseIssuerDTO> requests) {
        this.requests = requests;
    }
}
