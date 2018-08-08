package com.everhomes.rest.policy;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.RestResponseBase;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 分页游标</li>
 * <li>dtos: 查询记录列表 {@link com.everhomes.rest.policy.PolicyRecordDTO}</li>
 * </ul>
 */
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
