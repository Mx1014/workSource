package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 按钮文本的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-curr-processors-name")
public class FlowVarsTextButtonCurrentAllProcessorsName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowVarsButtonMsgCurrentProcessors flowVarsButtonMsgCurrentProcessors;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		List<Long> userIdList = flowVarsButtonMsgCurrentProcessors.variableUserResolve(
				ctx, null, null, null, null, 10);
		return displayText(flowService, ctx, userIdList, UserInfo::getNickName);
	}
}
