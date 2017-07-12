package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;

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
