package com.everhomes.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

@Component(FlowVariableUserResolver.APPLIER)
public class FlowVariableApplyUserResolver implements FlowVariableUserResolver {
	@Autowired
	FlowStateProcessor flowStateProcessor;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		return flowStateProcessor.getApplierSelection(ctx, userSelection);
	}

}
