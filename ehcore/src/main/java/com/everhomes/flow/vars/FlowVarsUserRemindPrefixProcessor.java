package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

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
