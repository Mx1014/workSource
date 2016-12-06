package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableResolver.APPLIER_PHONE)
public class FlowVaribleApplierNameResolver implements FlowVariableResolver {
	@Autowired
	private FlowStateProcessor flowStateProcessor;
	
	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowStateProcessor.getApplier(ctx, variable);
		if(userInfo == null || userInfo.getPhones() == null || userInfo.getPhones().size() == 0) {
			return "error";//TODO use error ?
		}
		return userInfo.getPhones().get(0);
	}

}
