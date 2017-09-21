package com.everhomes.flow;

/**
 * Created by xq.tian on 2017/9/19.
 */
public class FlowGraphLinkNormal extends FlowGraphLink {

    public FlowGraphNode getToNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException {
        return ctx.getFlowGraph().getGraphNode(flowLink.getToNodeId());
    }

    @Override
    public FlowGraphNode getFromNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException {
        return ctx.getFlowGraph().getGraphNode(flowLink.getFromNodeId());
    }
}
