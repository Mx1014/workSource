package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowBranchProcessMode;
import com.everhomes.rest.flow.FlowCaseStatus;
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
    public FlowCaseState processSubFlowCaseStart(FlowCaseState ctx, FlowGraphNode nextNode) {
        Long parentId = ctx.getFlowCase().getId();
        Long startLinkId = nextNode.getLinksIn().get(0).getFlowLink().getId();

        // 查原来是否已经创建了flowCase
        FlowCase subFlowCase = flowCaseProvider.findFlowCaseByStartLinkId(parentId, flowBranch.getOriginalNodeId(), flowBranch.getConvergenceNodeId(), startLinkId);
        if (subFlowCase == null) {
            subFlowCase = ConvertHelper.convert(ctx.getFlowCase(), FlowCase.class);
            subFlowCase.setId(null);
            subFlowCase.setParentId(parentId);
            subFlowCase.setStartNodeId(flowBranch.getOriginalNodeId());
            subFlowCase.setEndNodeId(flowBranch.getConvergenceNodeId());
            subFlowCase.setStartLinkId(startLinkId);
            subFlowCase.setStepCount(0L);
            subFlowCase.setStatus(FlowCaseStatus.PROCESS.getCode());
            flowCaseProvider.createFlowCase(subFlowCase);
        }
        subFlowCase.setStepCount(subFlowCase.getStepCount() + 1);
        return flowStateProcessor.prepareSubFlowCaseStart(ctx.getOperator(), subFlowCase);
    }
}
