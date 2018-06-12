package com.everhomes.flow.node;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;

public class FlowGraphNodeStart extends FlowGraphNode {

    transient private FlowStateProcessor flowStateProcessor;

	public FlowGraphNodeStart() {
		this(null);
	}

	public FlowGraphNodeStart(FlowNode flowNode) {
		this.setFlowNode(flowNode);
		flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
        flowStateProcessor.startStepEnter(ctx, from);
	}

	@Override
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException {	
	}

}
