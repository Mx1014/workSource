package com.everhomes.general_approval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.approval.ApprovalRequestDefaultHandler;
import com.everhomes.approval.ApprovalRequestHandler;
import com.everhomes.flow.FlowCase;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(GeneralApprovalHandler.GENERAL_APPROVAL_PREFIX + "ASK_FOR_LEAVE")
public class GeneralApprovalAskForLeaveHandler extends GeneralApprovalDefaultHandler {
	  
	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		//建立一个request
		
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseEnd(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}
	
}
