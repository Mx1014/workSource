package com.everhomes.rest.decoration;

import com.everhomes.rest.general_approval.PostApprovalFormItem;

import java.util.List;

public class PostApprovalFormCommand {

    private Long requestId;
    private Long approvalId;
    private List<PostApprovalFormItem> values;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }
}
