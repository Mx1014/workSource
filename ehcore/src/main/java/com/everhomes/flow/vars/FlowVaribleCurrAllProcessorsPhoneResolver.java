package com.everhomes.flow.vars;

import java.util.List;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.user.UserInfo;

/**
 * <ul>
 * <li>当前所有处理人手机号</li>
 * </ul>
 * @author janson
 *
 */
@Component(FlowVariableTextResolver.CURR_ALL_PROCESSORS_PHONE)
public class FlowVaribleCurrAllProcessorsPhoneResolver implements FlowVariableTextResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVaribleCurrAllProcessorsNameResolver.class);
	
	@Autowired
    FlowService flowService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		LOGGER.info("stackType" + ctx.peekProcessType());
		List<UserInfo> users = flowService.listUserSelectionsByNode(ctx, ctx.getCurrentNode().getFlowNode().getId());
		if(users != null && users.size() > 0) {
			String txt = "";
			for(UserInfo u : users) {
				if(u.getPhones() != null && u.getPhones().size() > 0) {
					txt += u.getPhones().get(0) + ", ";	
				}
				
			}
			if(txt.length() > 2) {
				txt = txt.substring(0, txt.length()-2);
			}
			return txt;
		}
		return null;
	}

}
