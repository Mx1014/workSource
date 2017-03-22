package com.everhomes.flow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

@Component(FlowVariableUserResolver.N_NODE_PROCESSOR)
public class FlowVariableNumberNodeProcessorResolver implements FlowVariableUserResolver {

	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		// TODO Auto-generated method stub
		return null;
	}

}
