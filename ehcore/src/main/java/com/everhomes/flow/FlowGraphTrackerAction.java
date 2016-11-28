package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowStepType;

public class FlowGraphTrackerAction extends FlowGraphAction {
	FlowStepType stepType;

	FlowGraphTrackerAction() {
		
	}
	
	FlowGraphTrackerAction(FlowStepType step) {
		this.stepType = step;
	}
	
	public FlowStepType getStepType() {
		return stepType;
	}

	public void setStepType(FlowStepType stepType) {
		this.stepType = stepType;
	}

	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		FlowEventLog log = new FlowEventLog();
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getModuleId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
		log.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
		log.setParentId(0l);
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(ctx.getOperator().getId());
		log.setFlowUserName(ctx.getOperator().getNickName());
		
		log.setLogType(FlowLogType.NODE_TRACKER.getCode());
		log.setTrackerApplier(this.getFlowAction().getTrackerApplier());
		log.setTrackerProcessor(this.getFlowAction().getTrackerProcessor());
		ctx.getLogs().add(log);
	}

}
