package com.everhomes.rest.policy;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.RestResponseBase;

import java.util.List;

public class PolicyRecordResponse extends RestResponseBase {
    private Long nextPageAnchor;
    @ItemType(PolicyRecordDTO.class)
    private List<PolicyRecordDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PolicyRecordDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<PolicyRecordDTO> dtos) {
        this.dtos = dtos;
    }
}
