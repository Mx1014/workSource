package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

@Component(FlowVariableUserResolver.PREFIX_NODE_PROCESSORS)
public class FlowVariablePrefixNodeProcessorsResolver implements FlowVariableUserResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariablePrefixNodeProcessorsResolver.class);
	
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
		
		FlowNode node = null;
		List<Long> rlts = new ArrayList<Long>();
		if(ctx.getPrefixNode() != null && ctx.getFlowCase() != null) {
			node = ctx.getPrefixNode().getFlowNode();		
		} else {
			FlowGraphNode currNode = ctx.getCurrentNode();
			if(currNode.getFlowNode().getNodeLevel().equals(1)) {
				//第一个节点，没有上个节点
				return rlts;
			}
			
			List<FlowEventLog> logs = flowEventLogProvider.findPrefixStepEventLogs(ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
			if(logs == null || logs.size() == 0) {
				return rlts;
			}
			for(int i = logs.size()-1; i >= 0; i--) {
				FlowGraphNode gnode = ctx.getFlowGraph().getGraphNode(logs.get(i).getFlowNodeId());
				if(gnode != null && gnode.getFlowNode().getNodeLevel() < currNode.getFlowNode().getNodeLevel()) {
					node = gnode.getFlowNode();
					break;
				}
			}
		}
		
		if(node != null) {
			List<FlowEventLog> logs = flowEventLogProvider.findPrefixNodeEnterLogs(node.getId(), ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
			if(logs != null && logs.size() > 0) {
				Long stepCount = logs.get(logs.size()-1).getStepCount();
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && stepCount.equals(log.getStepCount())) {
						rlts.add(log.getFlowUserId());
					}
				}	
			}
		}
		
		return rlts;
	}
	
}
