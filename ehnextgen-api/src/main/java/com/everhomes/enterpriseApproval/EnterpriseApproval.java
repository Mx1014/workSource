package com.everhomes.enterpriseApproval;

import com.everhomes.general_approval.GeneralApproval;

public class EnterpriseApproval extends GeneralApproval{

    public Long getApprovalGroupId() {
        return EnterpriseApprovalCustomField.APPROVAL_GROUP_ID.getIntegralValue(this);
    }
    public void setApprovalGroupId(Long approvalGroupId) {
        EnterpriseApprovalCustomField.APPROVAL_GROUP_ID.setIntegralValue(this, approvalGroupId);
    }

}
