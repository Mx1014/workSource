package com.everhomes.rest.policy;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.RestResponseBase;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 分页游标</li>
 * <li>dtos: 政策列表 {@link com.everhomes.rest.policy.PolicyDTO}</li>
 * </ul>
 */
public class PolicyResponse extends RestResponseBase {
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
