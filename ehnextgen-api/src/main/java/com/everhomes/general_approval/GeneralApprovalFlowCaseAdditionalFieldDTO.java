package com.everhomes.general_approval;

import com.everhomes.rest.flow.FlowCaseAdditionalFieldDTO;

public class GeneralApprovalFlowCaseAdditionalFieldDTO extends FlowCaseAdditionalFieldDTO {

    public Long getDepartmentId() {
        return GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.getIntegralValue(this);
    }

    public void setDepartmentId(Long departmentId){
        GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.setIntegralValue(this,departmentId);
    }

    public String getDepartment(){
        return GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.getStringValue(this);
    }

    public void setDepartment(String department){
        GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.setStringValue(this,department);
    }

    public Long getApprovalNo() {
        return GeneralApprovalFlowCaseCustomField.APPROVAL_NO.getIntegralValue(this);
    }

    public void setApprovalNo(Long approvalNo){
        GeneralApprovalFlowCaseCustomField.APPROVAL_NO.setIntegralValue(this,approvalNo);
    }
}
