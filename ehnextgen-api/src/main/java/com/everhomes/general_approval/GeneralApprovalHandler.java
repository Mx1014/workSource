package com.everhomes.general_approval;

import com.everhomes.flow.FlowCase;

public interface GeneralApprovalHandler { 
	static final String GENERAL_APPROVAL_PREFIX = "GeneralApprovalHandler_";

	void onFlowCaseCreating(FlowCase flowCase);

	void onFlowCaseAbsorted(FlowCase flowCase);

	void onFlowCaseEnd(FlowCase flowCase);
	
}
