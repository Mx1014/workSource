package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowBranchProcessMode;
import com.everhomes.util.ConvertHelper;

import java.util.List;

/**
 * Created by xq.tian on 2017/9/19.
 */
public class FlowGraphBranchNormal extends FlowGraphBranch {

    private FlowCaseProvider flowCaseProvider;
    private FlowStateProcessor flowStateProcessor;

    public FlowGraphBranchNormal() {
        flowCaseProvider = PlatformContext.getComponent(FlowCaseProvider.class);
        flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
    }

    @Override
    public FlowGraphNode getOriginalNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException {
        return ctx.getFlowGraph().getGraphNode(this.flowBranch.getOriginalNodeId());
    }

    @Override
    public FlowGraphNode getConvergenceNode(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException {
        return ctx.getFlowGraph().getGraphNode(this.flowBranch.getConvergenceNodeId());
    }

    @Override
    public List<FlowCase> listFlowCase(FlowCaseState ctx, FlowGraphEvent event) {
        return flowCaseProvider.findFlowCaseByNode(this.flowBranch.getOriginalNodeId(), this.flowBranch.getConvergenceNodeId());
    }

    @Override
    public boolean isConcurrent() {
        return FlowBranchProcessMode.fromCode(flowBranch.getProcessMode()) == FlowBranchProcessMode.CONCURRENT;
    }

    @Override
    public FlowCaseState prepareSubFlowCaseStart(FlowCaseState ctx) {
        FlowCase flowCase = ConvertHelper.convert(ctx.getFlowCase(), FlowCase.class);
        flowCase.setParentId(ctx.getFlowCase().getId());
        flowCase.setStartNodeId(flowBranch.getOriginalNodeId());
        flowCase.setEndNodeId(flowBranch.getConvergenceNodeId());
        return flowStateProcessor.prepareSubFlowCaseStart(ctx.getOperator(), flowCase);
    }
}
