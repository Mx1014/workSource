package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

/**
 * 节点跟踪的 上个节点操作执行人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-prefix-processor-name")
public class FlowVarsTextTrackerPrefixProcessorName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		if(ctx.getOperator() != null && ctx.getOperator().getId() > User.MAX_SYSTEM_USER_ID) {
			flowService.fixupUserInfoInContext(ctx, ctx.getOperator());
			return ctx.getOperator().getNickName();
		}
		
		return null;
	}

}
