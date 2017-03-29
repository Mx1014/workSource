package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.user.User;

/**
 * 按钮里面的 本节点操作执行人
 * @author janson
 *
 */
@Component("flow-variable-button-msg-curr-processor")
public class FlowVarsButtonMsgCurrentProcessor implements FlowVariableUserResolver {
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {
		List<Long> users = new ArrayList<Long>();
		if(ctx.getOperator() != null && ctx.getOperator().getId() > User.MAX_SYSTEM_USER_ID) {
			users.add(ctx.getOperator().getId());
		}

		return users;
	}

}
