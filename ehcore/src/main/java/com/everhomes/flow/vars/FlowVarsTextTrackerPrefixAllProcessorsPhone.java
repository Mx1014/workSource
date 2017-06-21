package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点跟踪的 上一步处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-prefix-processors-phone")
public class FlowVarsTextTrackerPrefixAllProcessorsPhone implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		/*FlowGraphNode currNode = ctx.getCurrentNode();
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
		}*/

        FlowNode node = null;
        if (ctx.getPrefixNode() != null) {
            node = ctx.getPrefixNode().getFlowNode();
        }

        if(node != null) {
            String txt = "";
            int i = 0;

            Long flowCaseId = ctx.getFlowCase().getId();
            Long maxStepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(node.getId(), flowCaseId);
            List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(node.getId(), flowCaseId, maxStepCount);
			if(logs != null && logs.size() > 0) {
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
						UserInfo ui = flowService.getUserInfoInContext(ctx, log.getFlowUserId());
						if(ui != null && ui.getPhones() != null && ui.getPhones().size() > 0) {
							txt += ui.getPhones().get(0) + ", ";
							
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
