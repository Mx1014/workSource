package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowStepType;
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
		FlowGraphNode next = ctx.getFlowGraph().getNodes().get(1);
		ctx.setNextNode(next);
		ctx.setStepType(FlowStepType.APPROVE_STEP);
		ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
		ctx.getFlowCase().setStepCount(ctx.getFlowCase().getStepCount()+1l);
	}

	@Override
	public Long getFiredButtonId() {
		return 0l;
	}

}
