package com.everhomes.flow;

import com.everhomes.rest.flow.FlowStepType;

public class FlowGraphNodeStart extends FlowGraphNode {

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
	public FlowStepType getStepType() {
		return FlowStepType.START_STEP;
	}

}
