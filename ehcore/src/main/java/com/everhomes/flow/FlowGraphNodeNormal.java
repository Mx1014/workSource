package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.RuntimeErrorException;

public class FlowGraphNodeNormal extends FlowGraphNode {
	private FlowEventLogProvider flowEventLogProvider;
	public FlowGraphNodeNormal() {
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);	
	}
	
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		FlowStepType fromStep = ctx.getStepType();
		FlowGraphNode curr = ctx.getCurrentNode();
		
		if(curr.getFlowNode().getNodeLevel() >= 1) {
			ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
		}
		ctx.getFlowCase().setCurrentNodeId(curr.getFlowNode().getId());
		boolean logStep = false;
		
		switch(fromStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			logStep = true;
			if(curr.getTrackApproveEnter() != null) {
				curr.getTrackApproveEnter().fireAction(ctx, null);	
			}
			
			break;
		case REJECT_STEP:
			logStep = true;
			if(curr.getTrackRejectEnter() != null) {
				curr.getTrackRejectEnter().fireAction(ctx, null);	
			}
			
			break;
		case TRANSFER_STEP:
			logStep = true;
			break;
		case COMMENT_STEP:
			break;
		case ABSORT_STEP:
			logStep = true;
			break;
		case REMINDER_STEP:
			logStep = true;
			break;
		case EVALUATE_STEP:
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
		FlowStepType fromStep = ctx.getStepType();
		FlowGraphNode curr = ctx.getCurrentNode();
		switch(fromStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			break;
		case REJECT_STEP:
			break;
		case TRANSFER_STEP:
			if(curr.getTrackTransferLeave() != null) {
				curr.getTrackTransferLeave().fireAction(ctx, null);	
			}
			
			break;
		case COMMENT_STEP:
			break;
		case ABSORT_STEP:
			break;
		case REMINDER_STEP:
			break;
		case EVALUATE_STEP:
			break;
		default:
			break;
		}		
	}

}
