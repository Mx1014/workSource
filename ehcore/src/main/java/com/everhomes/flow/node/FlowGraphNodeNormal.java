package com.everhomes.flow.node;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowStateProcessor;
import com.everhomes.flow.FlowStepErrorException;

public class FlowGraphNodeNormal extends FlowGraphNode {
	private FlowStateProcessor flowStateProcessor;
	
	public FlowGraphNodeNormal() {
		flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException {
		flowStateProcessor.normalStepEnter(ctx, from);
	}

	@Override
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException {
		flowStateProcessor.normalStepLeave(ctx, to);
	}
}
