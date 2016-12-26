package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.CURR_PROCESSOR_PHONE)
public class FlowVaribleCurrProcessorPhoneResolver implements FlowVariableTextResolver {
	@Autowired
	private FlowStateProcessor flowStateProcessor;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowStateProcessor.getCurrProcessor(ctx, variable);
		if(userInfo == null || userInfo.getPhones() == null || userInfo.getPhones().size() == 0) {
			return "error";//TODO use error ?
		}
		return userInfo.getNickName();
	}

}
