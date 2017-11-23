package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工标识号</li>
 * </ul>
 */
public class GetPersonnelDetailsV2Command {

    private Long detailId;


    public GetPersonnelDetailsV2Command() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
