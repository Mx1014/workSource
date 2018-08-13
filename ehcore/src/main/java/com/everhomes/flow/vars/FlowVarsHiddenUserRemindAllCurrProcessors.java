package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 所有任务节点的当前处理人
 */
@Component("flow-variable-hidden-button-msg-all-current-processors")
public class FlowVarsHiddenUserRemindAllCurrProcessors implements FlowVariableUserResolver {

	@Autowired
	FlowVarsButtonMsgCurrentProcessors flowVarsButtonMsgCurrentProcessors;

	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {

        List<Long> users = new ArrayList<>();
        for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
            users.addAll(flowVarsButtonMsgCurrentProcessors.variableUserResolve(
            		flowCaseState, processedEntities, fromEntity, entityId, userSelection, loopCnt));
        }
		return users.stream().distinct().collect(Collectors.toList());
	}
}
