package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.user.User;

/**
 * 按钮里面的 目标节点处理人
 * @author janson
 *
 */
@Component("flow-variable-button-msg-target-processors")
public class FlowVarsButtonMsgTargetProcessors implements FlowVariableUserResolver {
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {
		List<Long> users = new ArrayList<Long>();
		
		if(ctx.getNextNode() != null) {
			List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getNextNode().getFlowNode().getId()
					, ctx.getFlowCase().getId()
					, ctx.getFlowCase().getStepCount()); ////stepCount 不加 1 的原因是，目标节点处理人是当前 stepCount 计算的 node_enter 的值
			if(logs != null && logs.size() > 0) {
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
						users.add(log.getFlowUserId());	
					}
				}
			}	
		}
		
		
		return users;
	}

}
