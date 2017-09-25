package com.everhomes.flow;

import java.util.List;

public abstract class FlowGraphBranch {

	protected FlowBranch flowBranch;

    public FlowBranch getFlowBranch() {
        return flowBranch;
    }

    public void setFlowBranch(FlowBranch flowBranch) {
        this.flowBranch = flowBranch;
    }

    abstract public FlowGraphNode getOriginalNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;

    abstract public FlowGraphNode getConvergenceNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;

    public abstract List<FlowCase> listFlowCase(FlowCaseState ctx, FlowGraphEvent event);

    public abstract boolean isConcurrent();

    public abstract FlowCaseState processSubFlowCaseStart(FlowCaseState ctx, FlowGraphNode nextNode);
}
