// @formatter:off
package com.everhomes.group;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListAdminOpRequestCommandResponse {
    private Long nextPageAnchor;
    private List<GroupOpRequestDTO> requests;
    
    public ListAdminOpRequestCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GroupOpRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<GroupOpRequestDTO> requests) {
        this.requests = requests;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
