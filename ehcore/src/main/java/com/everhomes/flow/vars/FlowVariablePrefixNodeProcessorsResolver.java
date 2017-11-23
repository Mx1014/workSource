package com.everhomes.flow.vars;

import com.everhomes.flow.FlowVariableUserResolver;
import org.springframework.stereotype.Component;

@Component(FlowVariableUserResolver.PREFIX_NODE_PROCESSORS)
public class FlowVariablePrefixNodeProcessorsResolver extends FlowVarsUserRemindPrefixProcessors {
    /*private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariablePrefixNodeProcessorsResolver.class);

    @Autowired
    FlowUserSelectionProvider flowUserSelectionProvider;

    @Autowired
    FlowService flowService;

    @Autowired
    FlowEventLogProvider flowEventLogProvider;

    @Override
    public List<Long> variableUserResolve(FlowCaseState ctx,
                                          Map<String, Long> processedEntities, FlowEntityType fromEntity,
                                          Long entityId, FlowUserSelection userSelection, int loopCnt) {
        FlowNode node;
        List<FlowEventLog> logs = new ArrayList<>();

        if (ctx.getPrefixNode() != null && ctx.getFlowCase() != null) {
            node = ctx.getPrefixNode().getFlowNode();
            logs = flowEventLogProvider.findPrefixNodeEnterLogs(node.getId(),
                    ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
        } else {
            FlowGraphNode currNode = ctx.getCurrentNode();
            List<FlowGraphLink> linksIn = currNode.getLinksIn();
            if (linksIn.size() > 1) {
                for (FlowCaseState flowCaseState : ctx.getParentState().getChildStates()) {
                    FlowGraphLink flowLink = currNode.getFlowLink(flowCaseState.getFlowCase().getEndLinkId());
                    Long fromNodeId = flowLink.getFlowLink().getFromNodeId();

                    logs.addAll(flowEventLogProvider.findPrefixNodeEnterLogs(fromNodeId,
                            flowCaseState.getFlowCase().getId(), flowCaseState.getFlowCase().getStepCount()));
                }
            } else {
                FlowGraphNode fromNode = linksIn.get(0).getFromNode(ctx, null);
                node = fromNode.getFlowNode();
                if (node.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                    node = fromNode.getLinksIn().get(0).getFromNode(ctx, null).getFlowNode();
                }
                logs.addAll(flowEventLogProvider.findPrefixNodeEnterLogs(node.getId(),
                        ctx.getParentState().getFlowCase().getId(), ctx.getParentState().getFlowCase().getStepCount()));
            }
        }

        List<Long> rlts = new ArrayList<>();
        if (logs != null && logs.size() > 0) {
            Long stepCount = logs.get(logs.size() - 1).getStepCount();
            for (FlowEventLog log : logs) {
                if (log.getFlowUserId() != null && stepCount.equals(log.getStepCount())) {
                    rlts.add(log.getFlowUserId());
                }
            }
        }
        return rlts;
    }*/
}
