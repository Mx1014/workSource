package com.everhomes.flow;

import java.io.Serializable;

public abstract class FlowGraphAction implements Serializable {

	private FlowAction flowAction;

	public FlowAction getFlowAction() {
		return flowAction;
	}

	public void setFlowAction(FlowAction flowAction) {
		this.flowAction = flowAction;
	}
	
	public abstract void fireAction(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;
}
