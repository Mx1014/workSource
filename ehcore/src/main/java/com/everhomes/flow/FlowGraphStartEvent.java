package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;

public class FlowGraphStartEvent implements FlowGraphEvent {
	private Long firedUserId;
	
	@Override
	public FlowUserType getUserType() {
		return FlowUserType.APPLIER;
	}

	@Override
	public FlowEventType getEventType() {
		return FlowEventType.STEP_START;
	}

	@Override
	public Long getFiredUserId() {
		return firedUserId;
	}

	public void setFiredUserId(Long firedUserId) {
		this.firedUserId = firedUserId;
	}

	@Override
	public void fire(FlowCaseState ctx) {
	}

	@Override
	public Long getFiredButtonId() {
		return 0l;
	}

}
