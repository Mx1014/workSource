package com.everhomes.flow.action;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowStepType;

public class FlowGraphTrackerAction extends FlowGraphAction {
	FlowStepType stepType;
	private FlowService flowService;
	private FlowEventLogProvider flowEventLogProvider;

	FlowGraphTrackerAction() {
		this(null);
	}
	
	public FlowGraphTrackerAction(FlowStepType step) {
		flowService = PlatformContext.getComponent(FlowService.class);
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
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
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
		log.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
		log.setParentId(0l);
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(ctx.getOperator().getId());
		log.setFlowUserName(ctx.getOperator().getNickName());
		ctx.pushProcessType(FlowCaseStateStackType.TRACKER_ACTION);
		log.setLogContent(flowService.parseActionTemplate(ctx, this.getFlowAction().getId(), this.getFlowAction().getRenderText()));
		ctx.popProcessType();
		log.setStepCount(ctx.getFlowCase().getStepCount());
		log.setLogType(FlowLogType.NODE_TRACKER.getCode());
		log.setTrackerApplier(this.getFlowAction().getTrackerApplier());
		log.setTrackerProcessor(this.getFlowAction().getTrackerProcessor());
		
		if(event != null) {
			FlowSubject subject = event.getSubject();
			if(subject != null && subject.getContent() != null && !subject.getContent().isEmpty()) {
				log.setSubjectId(subject.getId());	
			} else {
				log.setSubjectId(0l);
			}
		}
		
		ctx.getLogs().add(log);
	}

}
