package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点提醒的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-remind-curr-processors-name")
public class FlowVarsTextRemindCurrentAllProcessorsName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(
		        ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
		String txt = "";
		int i = 0;
		
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

}
