package com.everhomes.enterpriseApproval;

import com.everhomes.enterpriseApproval.EnterpriseApprovalFlowCaseCustomField;
import com.everhomes.flow.FlowCase;

public class EnterpriseApprovalFlowCase extends FlowCase{

    public String getCreatorDepartment() {
        return EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.getStringValue(this);
    }

    public void setCreatorDepartment(String creatorDepartment) {
        EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.setStringValue(this, creatorDepartment);
    }

    public Long getCreatorDepartmentId() {
        return EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.getIntegralValue(this);
    }

    public void setCreatorDepartmentId(Long creatorDepartmentId) {
        EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.setIntegralValue(this, creatorDepartmentId);
    }

    public Long getApprovalNo() {
        return EnterpriseApprovalFlowCaseCustomField.APPROVAL_NO.getIntegralValue(this);
    }

    public void setApprovalNo(Long approvalNo) {
        EnterpriseApprovalFlowCaseCustomField.APPROVAL_NO.setIntegralValue(this, approvalNo);
    }

}
