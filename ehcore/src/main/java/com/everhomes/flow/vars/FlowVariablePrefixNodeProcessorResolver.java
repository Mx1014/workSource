package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.user.User;

/**
 * 上一步操作执行人
 * @author janson
 *
 */
@Component(FlowVariableUserResolver.PREFIX_NODE_PROCESSOR)
public class FlowVariablePrefixNodeProcessorResolver implements FlowVariableUserResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariablePrefixNodeProcessorResolver.class);
	
	@Autowired
    FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
    FlowService flowService;
	
	@Autowired
    FlowNodeProvider flowNodeProvider;
	
	@Autowired
    FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		//上个节点处理人是自己
		List<Long> users = new ArrayList<Long>();
		Long userId = ctx.getOperator().getId();
		if(userId > User.MAX_SYSTEM_USER_ID) {
			users.add(ctx.getOperator().getId());	
		}
		
		return users;
	}

}
