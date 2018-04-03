package com.everhomes.flow.vars;

import java.util.ArrayList;
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
 * 按钮文本的 本节点操作执行人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-curr-processor-phone")
public class FlowVarsTextButtonCurrentProcessorPhone implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		if(ctx.getOperator() != null && ctx.getOperator().getId() > User.MAX_SYSTEM_USER_ID) {
			UserInfo ui = flowService.getUserInfoInContext(ctx, ctx.getOperator().getId());
			if(ui != null && ui.getPhones() != null && ui.getPhones().size() > 0) {
				return ui.getPhones().get(0);
			}
		}
		
		return null;
	}

}
