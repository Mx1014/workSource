package com.everhomes.general_approval;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.techpark.punch.PunchExceptionRequest;

public interface GeneralApprovalHandler { 
	static final String GENERAL_APPROVAL_PREFIX = "GeneralApprovalHandler_";

	void onApprovalCreated(FlowCase flowCase);

	void onFlowCaseAbsorted(FlowCaseState flowCase);

	PunchExceptionRequest onFlowCaseEnd(FlowCase flowCase);
	
}
