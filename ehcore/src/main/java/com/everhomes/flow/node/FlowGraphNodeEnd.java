package com.everhomes.flow.node;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;

public class FlowGraphNodeEnd extends FlowGraphNode {
	private FlowStateProcessor flowStateProcessor;
	
	public FlowGraphNodeEnd() {
		this(null);
	}
	public FlowGraphNodeEnd(FlowNode flowNode) {
		this.setFlowNode(flowNode);
		flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		flowStateProcessor.endStepEnter(ctx, from);
	}

	@Override
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException {
	}

}
