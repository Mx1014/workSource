package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点提醒的 上个节点处理人
 * @author janson
 *
 */
@Component("flow-variable-text-remind-prefix-processors-name")
public class FlowVarsTextRemindPrefixAllProcessorsName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		FlowGraphNode currNode = ctx.getCurrentNode();
		if(currNode.getFlowNode().getNodeLevel().equals(1)) {
			//第一个节点，没有上个节点
			return null;
		}
		
		List<FlowEventLog> logs = flowEventLogProvider.findPrefixStepEventLogs(ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
		if(logs == null || logs.size() == 0) {
			return null;
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
			String txt = "";
			int i = 0;
			
			logs = flowEventLogProvider.findCurrentNodeEnterLogs(node.getId(), ctx.getFlowCase().getId(), stepLog.getStepCount());
			if(logs != null && logs.size() > 0) {
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
						UserInfo ui = flowService.getUserInfoInContext(ctx, log.getFlowUserId());
						if(ui != null) {
							txt += ui.getNickName() + ", ";
							
							i++;
							if(i >= 3) {
								break;
							}	
						}
						
					}
				}
			}
			
			if(txt.length() > 2) {
				txt = txt.substring(0, txt.length()-2);
			}
			return txt;
		}
		
		return null;
	}

}
