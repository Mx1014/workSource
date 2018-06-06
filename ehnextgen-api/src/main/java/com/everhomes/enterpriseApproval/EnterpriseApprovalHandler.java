package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.techpark.punch.PunchExceptionRequest;

public interface EnterpriseApprovalHandler {

    String ENTERPRISE_APPROVAL_PREFIX = "EnterpriseApprovalHandler_";

    void onApprovalCreated(FlowCase flowCase);

    void onFlowCaseAbsorted(FlowCaseState flowCase);

    //  add by wuhan.
    default PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase){return null;}

    default GeneralFormReminderDTO getGeneralFormReminder(GetTemplateBySourceIdCommand cmd){
        return new GeneralFormReminderDTO(TrueOrFalseFlag.FALSE.getCode());}
}
