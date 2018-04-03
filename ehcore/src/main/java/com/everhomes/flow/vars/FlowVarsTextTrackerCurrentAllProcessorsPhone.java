package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点跟踪的 本节点处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-curr-processors-phone")
public class FlowVarsTextTrackerCurrentAllProcessorsPhone implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
        List<Long> users = new ArrayList<>();

        Long maxStepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(
                ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId());
        List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(
                ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId(), maxStepCount);

        for (FlowEventLog log : logs) {
            if(log.getFlowUserId() != null) {
                users.add(log.getFlowUserId());
            }
        }

        for(FlowEventLog log : ctx.getLogs()) {
            if(log.getLogType().equals(FlowLogType.NODE_ENTER.getCode())) {
                if(log.getFlowUserId() != null) {
                    users.add(log.getFlowUserId());
                }
            }
        }

        users = users.stream().distinct().collect(Collectors.toList());
		
		String txt = "";
		int i = 0;
		for(Long u : users) {
			UserInfo ui = flowService.getUserInfoInContext(ctx, u);
			if(ui != null & ui.getPhones() != null && ui.getPhones().size() > 0) {
				txt += ui.getPhones().get(0) + ", ";
				
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
