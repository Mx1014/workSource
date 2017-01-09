package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.APPLIER_NAME)
public class FlowVaribleApplierPhoneResolver implements FlowVariableTextResolver {
	@Autowired
	private FlowStateProcessor flowStateProcessor;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowStateProcessor.getApplier(ctx, variable);
		if(userInfo == null) {
			return "error";//TODO use error ?
		}
		return userInfo.getNickName();
	}

}
