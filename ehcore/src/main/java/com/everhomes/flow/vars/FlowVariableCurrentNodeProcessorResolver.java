package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(FlowVariableUserResolver.CURRENT_NODE_PROCESSOR)
public class FlowVariableCurrentNodeProcessorResolver implements FlowVariableUserResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariableCurrentNodeProcessorResolver.class);
	
	@Autowired
    FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
    FlowService flowService;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity, Long entityId,
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
				LOGGER.warn("currentNode from graphNode not found flowCaseId= " + flowCase.getId() );				
			}
		}
		if(node == null) {
			LOGGER.warn("currentNode not found flowCaseId= " + flowCase.getId() );
			return new ArrayList<Long>();
		}
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(node.getId()
				, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode(), flowCase.getFlowVersion());
		return flowService.resolvUserSelections(ctx, processedEntities, FlowEntityType.FLOW_NODE, node.getId(), selections, loopCnt);
	}

}
