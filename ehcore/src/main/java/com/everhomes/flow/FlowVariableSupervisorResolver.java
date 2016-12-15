package com.everhomes.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

@Component(FlowVariableUserResolver.SUPERVISOR)
public class FlowVariableSupervisorResolver implements FlowVariableUserResolver {

	@Autowired
	FlowStateProcessor flowStateProcessor;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection) {
		return flowStateProcessor.getSupervisorSelection(ctx, userSelection);
	}

}
