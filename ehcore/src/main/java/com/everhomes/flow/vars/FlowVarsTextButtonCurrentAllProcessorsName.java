package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 按钮文本的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-curr-processors-name")
public class FlowVarsTextButtonCurrentAllProcessorsName extends FlowVarsButtonMsgCurrentProcessors implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		List<Long> userIdList = variableUserResolve(ctx, null, null, null, null, 10);

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Long uid : userIdList) {
			UserInfo ui = flowService.getUserInfoInContext(ctx, uid);
			if(ui != null) {
				sb.append(ui.getNickName()).append(", ");

				i++;
				if(i >= 3) {
					break;
				}
			}
		}

		if(sb.length() > 2) {
			return sb.substring(0, sb.length()-2);
		}
		return sb.toString();
	}
}
