package com.everhomes.flow;

import com.everhomes.rest.flow.FlowStepType;

public class FlowGraphTrackerAction extends FlowGraphAction {
	FlowStepType stepType;

	FlowGraphTrackerAction() {
		
	}
	
	FlowGraphTrackerAction(FlowStepType step) {
		this.stepType = step;
	}
	
	public FlowStepType getStepType() {
		return stepType;
	}

	public void setStepType(FlowStepType stepType) {
		this.stepType = stepType;
	}

	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
	}

}
