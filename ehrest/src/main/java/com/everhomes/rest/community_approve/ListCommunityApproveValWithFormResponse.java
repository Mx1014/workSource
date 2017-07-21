package com.everhomes.rest.community_approve;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

import java.util.List;

/**
 * Created by zhengsiting on 2017/7/21.
 */
public class ListCommunityApproveValWithFormResponse {

    CommunityApproveValDTO dto;

    List<PostApprovalFormItem> items;

    public CommunityApproveValDTO getDto() {
        return dto;
    }

    public void setDto(CommunityApproveValDTO dto) {
        this.dto = dto;
    }

    public List<PostApprovalFormItem> getItems() {
        return items;
    }

    public void setItems(List<PostApprovalFormItem> items) {
        this.items = items;
    }
}
