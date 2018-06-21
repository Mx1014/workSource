package com.everhomes.enterpriseApproval;

import com.everhomes.rest.general_approval.GeneralApprovalAdditionalFieldDTO;

public class EnterpriseApprovalAdditionFieldDTO extends GeneralApprovalAdditionalFieldDTO {

    public Long getApprovalGroupId() {
        return EnterpriseApprovalCustomField.APPROVAL_GROUP_ID.getIntegralValue(this);
    }

    public void setApprovalGroupId(Long approvalGroupId) {
        EnterpriseApprovalCustomField.APPROVAL_GROUP_ID.setIntegralValue(this, approvalGroupId);
    }
}
