package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;

public interface FlowGraphEvent {
	FlowUserType getUserType();
	FlowEventType getEventType();
	Long getFiredUserId();
	Long getFiredButtonId();
	
	void fire(FlowCaseState ctx);
}
