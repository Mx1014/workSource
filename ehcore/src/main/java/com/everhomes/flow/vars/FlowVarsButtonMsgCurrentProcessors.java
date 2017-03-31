package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowUserSelectionProvider;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserType;

/**
 * 按钮里面的 本节点处理人
 * @author janson
 *
 */
@Component("flow-variable-button-msg-curr-processors")
public class FlowVarsButtonMsgCurrentProcessors implements FlowVariableUserResolver {
	@Autowired
	FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {
		List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId()
				, ctx.getFlowCase().getStepCount()-1l); //stepCount-1 的原因是，当前节点处理人是上一个 stepCount 计算的 node_enter 的值
		List<Long> users = new ArrayList<Long>();
		if(logs != null && logs.size() > 0) {
			for(FlowEventLog log : logs) {
				if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
					users.add(log.getFlowUserId());	
				}
			}
		}
		
		return users;
	}

}
