package com.everhomes.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserType;

@Component(FlowVariableUserResolver.SUPERVISOR)
public class FlowVariableSupervisorResolver implements FlowVariableUserResolver {

	@Autowired
	FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
	FlowService flowService;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		FlowCase flowCase = ctx.getFlowCase();
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flowCase.getFlowMainId()
				, FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), flowCase.getFlowVersion());
		return flowService.resolvUserSelections(ctx, FlowEntityType.FLOW, null, selections, loopCnt);
	}

}
