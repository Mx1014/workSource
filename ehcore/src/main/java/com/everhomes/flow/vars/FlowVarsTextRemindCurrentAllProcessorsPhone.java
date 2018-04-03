package com.everhomes.flow.vars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

/**
 * 节点提醒的 本节点处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-remind-curr-processors-phone")
public class FlowVarsTextRemindCurrentAllProcessorsPhone implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId()
				, ctx.getFlowCase().getStepCount()); 
		String txt = "";
		int i = 0;
		
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

}
