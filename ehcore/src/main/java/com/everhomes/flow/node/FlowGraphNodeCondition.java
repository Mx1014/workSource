package com.everhomes.flow.node;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowBranchDecider;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.util.RuntimeErrorException;

import java.util.List;
import java.util.stream.Collectors;

public class FlowGraphNodeCondition extends FlowGraphNode {

    private FlowEventLogProvider flowEventLogProvider;

    public FlowGraphNodeCondition() {
        flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
    }

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
                    processCtx(ctx, branch, next);
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

        // 节点日志
        FlowEventLog log = new FlowEventLog();
        log.setId(flowEventLogProvider.getNextId());
        log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
        log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());//get real version
        log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
        if (ctx.getCurrentEvent() != null) {
            log.setFlowButtonId(ctx.getCurrentEvent().getFiredButtonId());
        }
        log.setFlowNodeId(this.getFlowNode().getId());
        log.setParentId(0L);
        log.setFlowCaseId(ctx.getFlowCase().getId());
        log.setFlowUserId(ctx.getOperator().getId());
        log.setStepCount(ctx.getFlowCase().getStepCount());
        log.setLogType(FlowLogType.NODE_ENTER.getCode());
        log.setLogTitle("");
        ctx.getLogs().add(log);
    }

    private void processCtx(FlowCaseState ctx, FlowGraphBranch branch, FlowGraphNode nextNode) {
        FlowCaseState subCtx = branch.processSubFlowCaseStart(ctx, nextNode);
        subCtx.setCurrentNode(this);
        subCtx.setNextNode(nextNode);
        subCtx.setParentState(ctx);
        subCtx.setStepType(FlowStepType.APPROVE_STEP);
        subCtx.setCurrentLane(ctx.getFlowGraph().getGraphLane(nextNode.getFlowNode().getFlowLaneId()));
        ctx.getChildStates().add(subCtx);
    }

    @Override
    public void stepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException {

    }
}