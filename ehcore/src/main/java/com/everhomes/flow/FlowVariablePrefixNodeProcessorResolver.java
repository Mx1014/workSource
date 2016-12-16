package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserType;

@Component(FlowVariableUserResolver.PREFIX_NODE_PROCESSOR)
public class FlowVariablePrefixNodeProcessorResolver implements FlowVariableUserResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariablePrefixNodeProcessorResolver.class);
	
	@Autowired
	FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowNodeProvider flowNodeProvider;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		FlowCase flowCase = ctx.getFlowCase();
		FlowNode node = null;
		if(ctx.getCurrentNode() != null) {
			node = ctx.getCurrentNode().getFlowNode();
		} else {
			FlowGraphNode graphNode = ctx.getFlowGraph().getGraphNode(flowCase.getCurrentNodeId());
			if(graphNode != null) {
				node = graphNode.getFlowNode();
			} else {
				LOGGER.warn("nextNode from graphNode not found flowCaseId= " + flowCase.getId() );				
			}
		}
		if(node == null || (node.getNodeLevel()-1) >= ctx.getFlowGraph().getNodes().size()) {
			LOGGER.warn("nextNode not found flowCaseId= " + flowCase.getId() );
			return new ArrayList<Long>();
		}
		
		//found fired buttons
//		flowEventLogProvider.findStepEventLogs(caseId, userId);
		
		FlowGraphNode graphNode = ctx.getFlowGraph().getNodes().get(node.getNodeLevel() - 1);
		node = graphNode.getFlowNode();
		
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(node.getId()
				, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode(), flowCase.getFlowVersion());
		return flowService.resolvUserSelections(ctx, FlowEntityType.FLOW_NODE, node.getId(), selections, loopCnt);
	}

}
