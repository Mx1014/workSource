package com.everhomes.flow;

public abstract class FlowGraphLink {

	protected FlowLink flowLink;

    public FlowLink getFlowLink() {
        return flowLink;
    }

    public void setFlowLink(FlowLink flowLink) {
        this.flowLink = flowLink;
    }

    abstract public FlowGraphNode enterLink(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;

    abstract public FlowGraphNode reverseLink(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;
}
