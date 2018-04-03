package com.everhomes.flow;

import java.io.Serializable;

public abstract class FlowGraphLink implements Serializable {

	protected FlowLink flowLink;

    public FlowLink getFlowLink() {
        return flowLink;
    }

    public void setFlowLink(FlowLink flowLink) {
        this.flowLink = flowLink;
    }

    abstract public FlowGraphNode getToNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;

    abstract public FlowGraphNode getFromNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;
}
