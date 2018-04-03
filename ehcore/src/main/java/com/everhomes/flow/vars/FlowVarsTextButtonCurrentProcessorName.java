package com.everhomes.flow.vars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

/**
 * 按钮文本的 本节点操作执行人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-curr-processor-name")
public class FlowVarsTextButtonCurrentProcessorName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		if(ctx.getOperator() != null && ctx.getOperator().getId() > User.MAX_SYSTEM_USER_ID) {
			flowService.fixupUserInfoInContext(ctx, ctx.getOperator());
			return ctx.getOperator().getNickName();
		}
		
		return null;
	}

}
