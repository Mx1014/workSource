package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

		List<Long> userIdList = logs.stream().map(FlowEventLog::getFlowUserId).collect(Collectors.toList());
		return displayText(flowService, ctx, userIdList, this::getPhone);
	}

}
