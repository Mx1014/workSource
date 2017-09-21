package com.everhomes.flow.node;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowBranchDecider;
import com.everhomes.util.RuntimeErrorException;

import java.util.List;
import java.util.stream.Collectors;

public class FlowGraphNodeCondition extends FlowGraphNode {

    @Override
    public void stepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException {
        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowGraphBranch branch = flowGraph.getBranchByOriginalNode(this.getFlowNode().getId());
        if (branch.isConcurrent()) {
            List<FlowGraphNode> nextNodes = getLinksOut().stream()
                    .map(FlowGraphLink::getFlowLink)
                    .map(FlowLink::getToNodeId)
                    .map(flowGraph::getGraphNode)
                    .collect(Collectors.toList());

            for (FlowGraphNode nextNode : nextNodes) {
                processCtx(ctx, branch, nextNode);
            }
        } else {
            FlowBranchDecider flowBranchDecider = FlowBranchDecider.fromCode(branch.getFlowBranch().getBranchDecider());
            // 用户选择下一步节点
            if (flowBranchDecider == FlowBranchDecider.PROCESSOR) {
                if (ctx.getCurrentEvent().getNextNodeId() != null) {
                    FlowGraphNode next = flowGraph.getGraphNode(ctx.getCurrentEvent().getNextNodeId());
                    ctx.setNextNode(next);
                } else {
                    throw RuntimeErrorException.errorWith("", 1, "please update your app");
                }
            } else if (flowBranchDecider == FlowBranchDecider.CONDITION) {
                FlowGraphCondition trueCond = null;
                for (FlowGraphCondition condition : this.getConditions()) {
                    if (condition.isTrue(ctx)) {
                        trueCond = condition;
                        break;
                    }
                }
                if (trueCond == null) {
                    trueCond = this.getConditions().get(this.getConditions().size() - 1);
                }

                Long nextNodeId = trueCond.getCondition().getNextNodeId();
                processCtx(ctx, branch, flowGraph.getGraphNode(nextNodeId));
            }
        }
    }

    private void processCtx(FlowCaseState ctx, FlowGraphBranch branch, FlowGraphNode nextNode) {
        FlowCaseState subCtx = branch.processSubFlowCaseStart(ctx);
        subCtx.setCurrentNode(this);
        subCtx.setNextNode(nextNode);
        ctx.getChildStates().add(subCtx);
    }

    @Override
    public void stepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException {

    }
}