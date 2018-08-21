package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 节点提醒的 上个节点操作执行人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-remind-prefix-processor-phone")
public class FlowVarsTextRemindPrefixProcessorPhone implements FlowVariableTextResolver {

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
