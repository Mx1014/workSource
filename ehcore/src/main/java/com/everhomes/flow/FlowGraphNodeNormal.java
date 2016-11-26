package com.everhomes.flow;

import com.everhomes.rest.flow.FlowStepType;

public class FlowGraphNodeNormal extends FlowGraphNode {

	@Override
	public FlowStepType getStepType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAction(FlowCaseState ctx, FlowStepType nextStep) {
		//TODO need this? the action is fired in action
	}

}
