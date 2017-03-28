package com.everhomes.flow;

import java.util.List;

import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;

public interface FlowGraphEvent {
	FlowUserType getUserType();
	FlowEventType getEventType();
	Long getFiredUserId();
	Long getFiredButtonId();
	public List<FlowEntitySel> getEntitySel();
	void fire(FlowCaseState ctx);
	FlowSubject getSubject();
}
