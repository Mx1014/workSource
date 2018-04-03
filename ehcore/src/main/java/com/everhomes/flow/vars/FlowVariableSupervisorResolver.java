package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component(FlowVariableUserResolver.SUPERVISOR)
public class FlowVariableSupervisorResolver implements FlowVariableUserResolver {

	@Autowired
    FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
    FlowService flowService;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity, Long entityId,
                                          FlowUserSelection userSelection, int loopCnt) {
		FlowCase flowCase = ctx.getFlowCase();
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flowCase.getFlowMainId()
				, FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), flowCase.getFlowVersion());
		return flowService.resolvUserSelections(ctx, processedEntities, FlowEntityType.FLOW, null, selections, loopCnt);
	}

}
