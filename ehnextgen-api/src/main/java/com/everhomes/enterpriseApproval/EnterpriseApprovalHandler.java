package com.everhomes.enterpriseApproval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.techpark.punch.PunchExceptionRequest;

public interface EnterpriseApprovalHandler {

    static final String ENTERPRISE_APPROVAL_PREFIX = "EnterpriseApprovalHandler_";

    void onApprovalCreated(FlowCase flowCase);

    void onFlowCaseAbsorted(FlowCaseState flowCase);

    PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase);
}
