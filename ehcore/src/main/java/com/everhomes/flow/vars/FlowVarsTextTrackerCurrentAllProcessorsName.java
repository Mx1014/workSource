package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

/**
 * 节点跟踪的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-curr-processors-name")
public class FlowVarsTextTrackerCurrentAllProcessorsName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		Map<Long, Long> userMap = new HashMap<Long, Long>();
		List<Long> users = new ArrayList<Long>();
		for(FlowEventLog log : ctx.getLogs()) {
			if(log.getLogType().equals(FlowLogType.NODE_ENTER.getCode())) {
				if(log.getFlowUserId() != null && !userMap.containsKey(log.getFlowUserId())) {
					users.add(log.getFlowUserId());
					userMap.put(log.getFlowUserId(), 1l);
				}
			}
		}
		
		String txt = "";
		int i = 0;
		for(Long u : users) {
			UserInfo ui = flowService.getUserInfoInContext(ctx, u);
			if(ui != null) {
				txt += ui.getNickName() + ", ";
				
				i++;
				if(i >= FlowConstants.FLOW_MAX_NAME_CNT) {
					break;
				}	
			}	
		}
		
		if(txt.length() > 2) {
			txt = txt.substring(0, txt.length()-2);
		}
		return txt;
		
	}

}
