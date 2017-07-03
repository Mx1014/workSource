package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

@Component(FlowVariableTextResolver.CURR_PROCESSOR_PHONE)
public class FlowVaribleCurrProcessorPhoneResolver implements FlowVariableTextResolver {
	@Autowired
    FlowService flowService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		UserInfo userInfo = flowService.getCurrProcessor(ctx, variable);
		if(userInfo == null || userInfo.getPhones() == null || userInfo.getPhones().size() == 0) {
			return "error";//TODO use error ?
		}
		return userInfo.getPhones().get(0);
	}

}
