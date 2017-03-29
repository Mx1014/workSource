package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

/**
 * 节点消息里面的 上一步处理人
 * @author janson
 *
 */
@Component("flow-variable-user-remind-prefix-processors")
public class FlowVarsUserRemindPrefixProcessors implements FlowVariableUserResolver {
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {
		List<Long> users = new ArrayList<Long>();
		
		FlowGraphNode currNode = ctx.getCurrentNode();
		if(currNode.getFlowNode().getNodeLevel().equals(1)) {
			//第一个节点，没有上个节点
			return users;
		}
		
		List<FlowEventLog> logs = flowEventLogProvider.findPrefixStepEventLogs(ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
		if(logs == null || logs.size() == 0) {
			return users;
		}
		
		FlowNode node = null;
		FlowEventLog stepLog = null;
		for(int i = logs.size()-1; i >= 0; i--) {
			stepLog = logs.get(i);
			FlowGraphNode gnode = ctx.getFlowGraph().getGraphNode(stepLog.getFlowNodeId());
			if(gnode != null && gnode.getFlowNode().getNodeLevel() < currNode.getFlowNode().getNodeLevel()) {
				node = gnode.getFlowNode();
				break;
			}
		}
		
		if(node != null) {
			logs = flowEventLogProvider.findCurrentNodeEnterLogs(node.getId(), ctx.getFlowCase().getId(), stepLog.getStepCount());
			if(logs != null && logs.size() > 0) {
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
						users.add(log.getFlowUserId());	
					}
				}
			}
		}
		
		return users;
	}

}
