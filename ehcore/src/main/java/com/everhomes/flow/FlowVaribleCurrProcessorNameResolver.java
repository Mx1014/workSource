package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.CURR_PROCESSOR_NAME)
public class FlowVaribleCurrProcessorNameResolver implements FlowVariableTextResolver {
	@Autowired
	private FlowStateProcessor flowStateProcessor;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowStateProcessor.getCurrProcessor(ctx, variable);
		if(userInfo == null || userInfo.getNickName() == null) {
			return "error";//TODO use error ?
		}
		return userInfo.getNickName();
	}

}
