package com.everhomes.rest.policy;

import com.everhomes.discover.ItemType;

import java.util.List;

public class PolicyRecordResponse {
    private Long nextPageAnchor;
    @ItemType(PolicyDTO.class)
    private List<PolicyDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PolicyDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<PolicyDTO> dtos) {
        this.dtos = dtos;
    }
}
