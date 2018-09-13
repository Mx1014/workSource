package com.everhomes.flow.node;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;

public class FlowGraphNodeSubFlow extends FlowGraphNode {

    transient private FlowStateProcessor flowStateProcessor;

    public FlowGraphNodeSubFlow(FlowNode flowNode) {
        this.setFlowNode(flowNode);
        flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
    }

    @Override
    public void stepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException {
        flowStateProcessor.subflowStepEnter(ctx, from);
    }

    @Override
    public void stepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException {
        flowStateProcessor.subflowStepLeave(ctx, this, to);
    }
}
