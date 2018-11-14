package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.APPLIER_NAME)
public class FlowVaribleApplierNameResolver implements FlowVariableTextResolver {
	@Autowired
    FlowService flowService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowService.getUserInfoInContext(ctx, ctx.getFlowCase().getApplyUserId());
		if(userInfo == null) {
			return "error";//TODO use error ?
		}
		return userInfo.getNickName();
	}

}
