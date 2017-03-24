package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.user.User;

public class FlowGraphNoStepEvent implements FlowGraphEvent {
	FlowAutoStepDTO stepDTO;
	private Long firedUserId;
	
	public FlowGraphNoStepEvent() {
		this(null);
	}
	
	public FlowGraphNoStepEvent(FlowAutoStepDTO o) {
		firedUserId = User.SYSTEM_UID;
		if(o.getOperatorId() != null) {
			firedUserId = o.getOperatorId();
		}
		this.stepDTO = o;
	}

	@Override
	public FlowUserType getUserType() {
		return FlowUserType.PROCESSOR;
	}

	@Override
	public FlowEventType getEventType() {
		return FlowEventType.STEP_TIMEOUT;
	}

	public void setFiredUserId(Long firedUserId) {
		this.firedUserId = firedUserId;
	}

	@Override
	public Long getFiredUserId() {
		return this.firedUserId;
	}

	@Override
	public Long getFiredButtonId() {
		return null;
	}
	
	@Override
	public List<FlowEntitySel> getEntitySel() {
		return new ArrayList<FlowEntitySel>();
	}

	@Override
	public void fire(FlowCaseState ctx) {
	}
}
