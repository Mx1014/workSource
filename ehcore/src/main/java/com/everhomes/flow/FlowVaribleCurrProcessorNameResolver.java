package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableResolver.CURR_PROCESSOR_NAME)
public class FlowVaribleCurrProcessorNameResolver implements FlowVariableResolver {
	@Autowired
	private FlowStateProcessor flowStateProcessor;
	
	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowStateProcessor.getApplier(ctx, variable);
		if(userInfo == null) {
			return "error";//TODO use error ?
		}
		return userInfo.getNickName();
	}

}
