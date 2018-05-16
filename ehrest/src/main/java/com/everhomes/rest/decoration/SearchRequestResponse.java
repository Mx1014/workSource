package com.everhomes.rest.decoration;


import java.util.List;

public class SearchRequestResponse {

    private Long nextPageAnchor;
    private List<DecorationRequestDTO> requests;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DecorationRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<DecorationRequestDTO> requests) {
        this.requests = requests;
    }
}
