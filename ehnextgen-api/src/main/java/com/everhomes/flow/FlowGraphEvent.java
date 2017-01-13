package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;

public interface FlowGraphEvent {
	FlowUserType getUserType();
	FlowEventType getEventType();
	Long getFiredUserId();
	Long getFiredButtonId();
	public Long getEntityId();
	public String getFlowEntityType();
	void fire(FlowCaseState ctx);
}
