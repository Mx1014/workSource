package com.everhomes.flow;

public class FlowGraphMessageAction extends FlowGraphAction {

	private Long timeoutAtTick;
	
	public FlowGraphMessageAction() {
		
	}
	
	public FlowGraphMessageAction(Long timeoutAtTick) {
		this.timeoutAtTick = timeoutAtTick;
	}
	
	public Long getTimeoutAtTick() {
		return timeoutAtTick;
	}

	public void setTimeoutAtTick(Long timeoutAtTick) {
		this.timeoutAtTick = timeoutAtTick;
	}

	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		
	}

}
