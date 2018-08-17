package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 节点消息里面的 上一步操作执行人
 * @author janson
 *
 */
@Component("flow-variable-user-remind-prefix-processor")
public class FlowVarsUserRemindPrefixProcessor implements FlowVariableUserResolver {
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {
		List<Long> users = new ArrayList<Long>();
		
		Long userId = ctx.getOperator().getId();
		if(userId > User.MAX_SYSTEM_USER_ID) {
			users.add(ctx.getOperator().getId());	
		}
		return users;
	}

}
