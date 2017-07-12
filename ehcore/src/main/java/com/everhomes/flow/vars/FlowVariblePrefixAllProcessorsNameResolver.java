package com.everhomes.flow.vars;

import java.util.List;

import com.everhomes.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.PREFIX_ALL_PROCESSORS_NAME)
public class FlowVariblePrefixAllProcessorsNameResolver implements FlowVariableTextResolver {
private static final Logger LOGGER = LoggerFactory.getLogger(FlowVaribleCurrAllProcessorsNameResolver.class);
	
	@Autowired
    FlowService flowService;
	
	@Autowired
    FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		FlowNode node = null;
		if(ctx.getPrefixNode() != null) {
			node = ctx.getPrefixNode().getFlowNode();
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
		
		//TODO implement like FlowVariablePrefixNodeProcessorsResolver ?
		if(node != null) {
			List<UserInfo> users = flowService.listUserSelectionsByNode(ctx, node.getId());
			if(users != null && users.size() > 0) {
				String txt = "";
				for(UserInfo u : users) {
					txt += u.getNickName() + ", ";
				}
				if(txt.length() > 2) {
					txt = txt.substring(0, txt.length()-2);
				}
				return txt;
			}
		}
		
		
		return null;
	}

}
