package com.everhomes.rest.community_approve;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

import java.util.List;

/**
 * Created by zhengsiting on 2017/7/20.
 */
public class PostCommunityApproveFormCommand {
    private Long approvalId;
    private Long organizationId;

    @ItemType(PostApprovalFormItem.class)
    private List<PostApprovalFormItem> values;
    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }
}
