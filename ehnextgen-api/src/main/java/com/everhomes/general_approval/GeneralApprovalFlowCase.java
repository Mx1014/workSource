package com.everhomes.general_approval;

import com.everhomes.flow.FlowCase;

public class GeneralApprovalFlowCase extends FlowCase{

    public String getCreatorDepartment() {
        return GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.getStringValue(this);
    }

    public void setCreatorDepartment(String creatorDepartment) {
        GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.setStringValue(this, creatorDepartment);
    }

    public Long getCreatorDepartmentId() {
        return GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.getIntegralValue(this);
    }

    public void setCreatorDepartmentId(Long creatorDepartmentId) {
        GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.setIntegralValue(this, creatorDepartmentId);
    }

    public Long getApprovalNo() {
        return GeneralApprovalFlowCaseCustomField.APPROVAL_NO.getIntegralValue(this);
    }

    public void setApprovalNo(Long approvalNo) {
        GeneralApprovalFlowCaseCustomField.APPROVAL_NO.setIntegralValue(this, approvalNo);
    }

}
