package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowNodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(FlowVariableUserResolver.PREFIX_NODE_PROCESSORS)
public class FlowVariablePrefixNodeProcessorsResolver implements FlowVariableUserResolver {

    @Autowired
    FlowEventLogProvider flowEventLogProvider;

    @Override
    public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity, Long entityId, FlowUserSelection userSelection, int loopCnt) {
        FlowNode node;
        List<FlowEventLog> logs = new ArrayList<>();

        FlowGraphNode currNode = ctx.getCurrentNode();
        List<FlowGraphLink> linksIn = currNode.getLinksIn();
        if (linksIn.size() > 1) {
            FlowCaseState parentState = ctx.getParentState();
            if (parentState != null) {
                for (FlowCaseState flowCaseState : parentState.getChildStates()) {
                    FlowGraphLink flowLink = currNode.getFlowLink(flowCaseState.getFlowCase().getEndLinkId());
                    Long fromNodeId = flowLink.getFlowLink().getFromNodeId();

                    Long maxStepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(fromNodeId, flowCaseState.getFlowCase().getId());
                    logs.addAll(flowEventLogProvider.findPrefixNodeEnterLogs(fromNodeId,
                            flowCaseState.getFlowCase().getId(), maxStepCount + 1));
                }
            }
        } else {
            FlowGraphNode fromNode = linksIn.get(0).getFromNode(ctx, null);
            node = fromNode.getFlowNode();
            if (node.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                node = fromNode.getLinksIn().get(0).getFromNode(ctx, null).getFlowNode();
                ctx = ctx.getParentState();
            }
            Long maxStepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(node.getId(), ctx.getFlowCase().getId());
            logs.addAll(flowEventLogProvider.findPrefixNodeEnterLogs(node.getId(),
                    ctx.getFlowCase().getId(), maxStepCount + 1));
        }

        List<Long> rlts = new ArrayList<>();
        if (logs.size() > 0) {
            Long stepCount = logs.get(logs.size() - 1).getStepCount();
            for (FlowEventLog log : logs) {
                if (log.getFlowUserId() != null && stepCount.equals(log.getStepCount())) {
                    rlts.add(log.getFlowUserId());
                }
            }
        }
        return rlts;
    }
}
