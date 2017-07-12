package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;

public class FlowGraphNodeNormal extends FlowGraphNode {
	private FlowStateProcessor flowStateProcessor;
	
	public FlowGraphNodeNormal() {
		flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		flowStateProcessor.normalStepEnter(ctx, from);
	}

	@Override
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException {
		flowStateProcessor.normalStepLeave(ctx, to);
	}

}
