package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.APPLIER_PHONE)
public class FlowVaribleApplierNameResolver implements FlowVariableTextResolver {
	@Autowired
	FlowService flowService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowService.getUserInfoInContext(ctx, ctx.getFlowCase().getApplyUserId());
		if(userInfo == null || userInfo.getPhones() == null || userInfo.getPhones().size() == 0) {
			return "error";//TODO use error ?
		}
		return userInfo.getPhones().get(0);
	}

}
