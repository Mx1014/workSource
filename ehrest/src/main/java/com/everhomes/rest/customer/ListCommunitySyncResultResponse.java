package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by ying.xiong on 2018/1/15.
 */
public class ListCommunitySyncResultResponse {

    @ItemType(SyncDataResult.class)
    private List<SyncDataResult> results;

    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SyncDataResult> getResults() {
        return results;
    }

    public void setResults(List<SyncDataResult> results) {
        this.results = results;
    }
}
