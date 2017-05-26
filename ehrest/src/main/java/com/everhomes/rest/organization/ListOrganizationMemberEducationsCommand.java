package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工编号(Education.detail_id),注意不是教育信息的id</li>
 * </ul>
 */
public class ListOrganizationMemberEducationsCommand {


    private Long detailId;

    public ListOrganizationMemberEducationsCommand() {
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
