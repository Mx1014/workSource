package com.everhomes.flow;

public abstract class FlowGraphAction {
	private FlowAction flowAction;

	public FlowAction getFlowAction() {
		return flowAction;
	}

	public void setFlowAction(FlowAction flowAction) {
		this.flowAction = flowAction;
	}
	
	public abstract void fireAction(FlowCaseState ctx, FlowGraphEvent event) throws FlowStepErrorException;

}
