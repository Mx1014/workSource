package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 上个节点的执行人名字
 * @author janson
 *
 */
@Component(FlowVariableTextResolver.PREFIX_PROCESSOR_NAME)
public class FlowVariblePrefixProcessorNameResolver implements FlowVariableTextResolver {
private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariblePrefixProcessorNameResolver.class);
	
	@Autowired
    FlowService flowService;
	
	@Autowired
    FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		FlowNode node = null;
		if(ctx.getPrefixNode() != null) {
			node = ctx.getPrefixNode().getFlowNode();
			if(node != null && ctx.getCurrentNode().getFlowNode().getId().equals(node.getId())) {
				node = null;
			}
		}
		if(node == null) {
			FlowGraphNode currNode = ctx.getCurrentNode();
			if(currNode.getFlowNode().getNodeLevel().equals(1)) {
				//第一个节点，没有上个节点
				return null;
			}
			
			List<FlowEventLog> logs = flowEventLogProvider.findPrefixStepEventLogs(ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
			if(logs == null || logs.size() == 0) {
				return null;
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
			UserInfo user = flowService.getPrefixProcessor(ctx, node.getId());
			if(user != null) {
				return user.getNickName();
			}
		}
		
		
		return null;
	}

}
