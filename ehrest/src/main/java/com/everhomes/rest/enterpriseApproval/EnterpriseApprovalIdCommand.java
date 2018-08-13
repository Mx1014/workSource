package com.everhomes.rest.enterpriseApproval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>approvalId : 审批id</li>
 * </ul>
 * */
public class EnterpriseApprovalIdCommand {
    private Long approvalId;

    public EnterpriseApprovalIdCommand() {
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
