package com.everhomes.flow.event;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphEvent;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowSubject;
import com.everhomes.rest.flow.*;

import java.util.ArrayList;
import java.util.List;

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
		FlowGraphNode next = ctx.getFlowGraph().getNodes().get(1);
		ctx.setNextNode(next);
		ctx.setStepType(FlowStepType.APPROVE_STEP);
		ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
		ctx.getFlowCase().setStepCount(ctx.getFlowCase().getStepCount() + 1L);
	}

	@Override
	public Long getFiredButtonId() {
		return 0L;
	}

	@Override
	public List<FlowEntitySel> getEntitySel() {
		return new ArrayList<>();
	}

	@Override
	public FlowSubject getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

}
