package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 当前所有节点处理人名字
 * @author janson
 *
 */
@Component(FlowVariableTextResolver.CURR_ALL_PROCESSORS_NAME)
public class FlowVaribleCurrAllProcessorsNameResolver implements FlowVariableTextResolver {
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
				txt += u.getNickName() + ", ";
			}
			if(txt.length() > 2) {
				txt = txt.substring(0, txt.length()-2);
			}
			return txt;
		}
		return null;
	}

}
