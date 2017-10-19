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
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {

        List<Long> users = new ArrayList<Long>();

        for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
            if (ctx.getCurrentNode() == null) {
                continue;
            }
            List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(
                    flowCaseState.getCurrentNode().getFlowNode().getId(),
                    flowCaseState.getFlowCase().getId(),
                    flowCaseState.getFlowCase().getStepCount()
            );

            if(logs != null && logs.size() > 0) {
                for(FlowEventLog log : logs) {
                    if(log.getFlowUserId() != null && log.getFlowUserId() > 0 && log.getStepCount() > -1) {
                        users.add(log.getFlowUserId());
                    }
                }
            }
        }
		return users.stream().distinct().collect(Collectors.toList());
	}
}
