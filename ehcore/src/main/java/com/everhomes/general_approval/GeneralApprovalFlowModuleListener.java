package com.everhomes.general_approval;

import java.util.List;

import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowUserType;

public abstract class GeneralApprovalFlowModuleListener implements FlowModuleListener {

	@Override
	public FlowModuleInfo initModule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowCreating(Flow flow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		if(flowCase.getReferType().equals(FlowReferType.APPROVAL.getCode())){
			//TODO 
		}
		return null;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub

	}

}
