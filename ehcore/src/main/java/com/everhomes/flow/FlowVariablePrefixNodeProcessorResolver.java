package com.everhomes.flow;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

@Component(FlowVariableUserResolver.PREFIX_NODE_PROCESSOR)
public class FlowVariablePrefixNodeProcessorResolver implements FlowVariableUserResolver {

	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection) {
		// TODO Auto-generated method stub
		return null;
	}

}
