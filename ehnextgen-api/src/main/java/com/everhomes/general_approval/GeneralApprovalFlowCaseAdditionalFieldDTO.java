package com.everhomes.general_approval;

import com.everhomes.enterpriseApproval.EnterpriseApprovalFlowCaseCustomField;
import com.everhomes.rest.flow.FlowCaseAdditionalFieldDTO;

public class GeneralApprovalFlowCaseAdditionalFieldDTO extends FlowCaseAdditionalFieldDTO {

    public Long getDepartmentId() {
        return EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.getIntegralValue(this);
    }

    public void setDepartmentId(Long departmentId){
        EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.setIntegralValue(this,departmentId);
    }

    public String getDepartment(){
        return EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.getStringValue(this);
    }

    public void setDepartment(String department){
        EnterpriseApprovalFlowCaseCustomField.CREATOR_DEPARTMENT.setStringValue(this,department);
    }

    public Long getApprovalNo() {
        return EnterpriseApprovalFlowCaseCustomField.APPROVAL_NO.getIntegralValue(this);
    }

    public void setApprovalNo(Long approvalNo){
        EnterpriseApprovalFlowCaseCustomField.APPROVAL_NO.setIntegralValue(this,approvalNo);
    }
}
