package com.everhomes.rest.decoration;


import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor</li>
 * <li>requests : List 参考{@link com.everhomes.rest.decoration.DecorationRequestDTO}</li>
 * </ul>
 */
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
