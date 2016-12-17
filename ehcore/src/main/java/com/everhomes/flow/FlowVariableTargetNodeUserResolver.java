package com.everhomes.flow;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

@Component(FlowVariableUserResolver.TARGET_NODE_PROCESSOR)
public class FlowVariableTargetNodeUserResolver implements FlowVariableUserResolver {

	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		return null;
	}

}
