package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工编号</li>
 * </ul>
 */
public class GetProfileIntegrityCommand {

    private Long detailId;

    public GetProfileIntegrityCommand() {
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

