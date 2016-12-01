package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;

public class FlowGraphNodeEnd extends FlowGraphNode {
	private FlowEventLogProvider flowEventLogProvider;
	
	public FlowGraphNodeEnd() {
		this(null);
	}
	public FlowGraphNodeEnd(FlowNode flowNode) {
		this.setFlowNode(flowNode);
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		FlowStepType fromStep = ctx.getStepType();
		FlowGraphNode curr = ctx.getCurrentNode();
		ctx.getFlowCase().setCurrentNodeId(curr.getFlowNode().getId());
		boolean logStep = false;
		
		switch(fromStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			ctx.getFlowCase().setStatus(FlowCaseStatus.FINISHED.getCode());
			break;
		case EVALUATE_STEP:
			break;
		case ABSORT_STEP:
			logStep = true;
			ctx.getFlowCase().setStatus(FlowCaseStatus.ABSORTED.getCode());
			break;
		default:
			break;
		}
		
		if(logStep) {
			UserInfo firedUser = ctx.getOperator();
			FlowEventLog log = new FlowEventLog();
			log.setId(flowEventLogProvider.getNextId());
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getModuleId());
			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			log.setFlowNodeId(curr.getFlowNode().getId());
			log.setParentId(0l);
			log.setFlowCaseId(ctx.getFlowCase().getId());
			if(firedUser != null) {
				log.setFlowUserId(firedUser.getId());
				log.setFlowUserName(firedUser.getNickName());	
			}
			log.setButtonFiredStep(fromStep.getCode());
			log.setLogType(FlowLogType.STEP_TRACKER.getCode());
			log.setStepCount(ctx.getFlowCase().getStepCount());
			ctx.getLogs().add(log);	//added but not save to database now.
		}
	}

	@Override
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException {
		// TODO Auto-generated method stub
		
	}

}
