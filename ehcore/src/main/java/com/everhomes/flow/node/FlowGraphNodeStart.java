package com.everhomes.flow.node;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;

import java.util.List;

public class FlowGraphNodeStart extends FlowGraphNode {
	private FlowEventLogProvider flowEventLogProvider;
	
	public FlowGraphNodeStart() {
		this(null);
	}
	
	public FlowGraphNodeStart(FlowNode flowNode) {
		this.setFlowNode(flowNode);
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
	}

	@Override
	public void stepLeave(FlowCaseState ctx, List<FlowGraphNode> to)
			throws FlowStepErrorException {	
	}

}
