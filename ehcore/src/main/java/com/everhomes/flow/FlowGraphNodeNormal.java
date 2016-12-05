package com.everhomes.flow;

import java.sql.Timestamp;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowTimeoutStepDTO;
import com.everhomes.rest.flow.FlowTimeoutType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

public class FlowGraphNodeNormal extends FlowGraphNode {
	private FlowEventLogProvider flowEventLogProvider;
	private FlowTimeoutService flowTimeoutService;
	
	public FlowGraphNodeNormal() {
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
		flowTimeoutService = PlatformContext.getComponent(FlowTimeoutService.class);
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
		
		FlowEventLog log = null;
//		log = flowEventLogProvider.getStepEvent(ctx.getFlowCase().getId(), curr.getFlowNode().getId(), ctx.getFlowCase().getStepCount(), fromStep);
//		if(log != null && fromStep != FlowStepType.COMMENT_STEP) {
//			return;
//		}
		
		if(curr.getMessageAction() != null) {
			curr.getMessageAction().fireAction(ctx, ctx.getCurrentEvent());
		}
		if(curr.getSmsAction() != null) {
			curr.getSmsAction().fireAction(ctx, ctx.getCurrentEvent());
		}
		if(curr.getTickMessageAction() != null) {
			curr.getTickMessageAction().fireAction(ctx, ctx.getCurrentEvent());
		}
		if(curr.getTickSMSAction() != null) {
			curr.getTickSMSAction().fireAction(ctx, ctx.getCurrentEvent());
		}
		
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
			break;
		case COMMENT_STEP:
			break;
		case ABSORT_STEP:
			logStep = true;
			break;
		case REMINDER_STEP:
			break;
		case EVALUATE_STEP:
			break;
		default:
			break;
		}
		
		//create step timeout
		if(!curr.getFlowNode().getAutoStepMinute().equals(0l)) {
			FlowTimeout ft = new FlowTimeout();
			ft.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
			ft.setBelongTo(curr.getFlowNode().getId());
			ft.setTimeoutType(FlowTimeoutType.STEP_TIMEOUT.getCode());
			ft.setStatus(FlowStatusType.VALID.getCode());
			curr.getFlowNode().getAutoStepType();
			
			FlowTimeoutStepDTO stepDTO = new FlowTimeoutStepDTO();
			stepDTO.setFlowCaseId(ctx.getFlowCase().getId());
			stepDTO.setFlowMainId(ctx.getFlowCase().getFlowMainId());
			stepDTO.setFlowVersion(ctx.getFlowCase().getFlowVersion());
			stepDTO.setStepCount(ctx.getFlowCase().getStepCount());
			stepDTO.setFlowNodeId(curr.getFlowNode().getId());
			stepDTO.setAutoStepType(curr.getFlowNode().getAutoStepType());
			ft.setJson(stepDTO.toString());
			
			Long timeoutTick = DateHelper.currentGMTTime().getTime() + curr.getFlowNode().getAutoStepMinute().intValue() * 60*1000;
			ft.setTimeoutTick(new Timestamp(timeoutTick));
			
			flowTimeoutService.pushTimeout(ft);
		}
		
		if(logStep && log == null) {
			UserInfo firedUser = ctx.getOperator();
			log = new FlowEventLog();
			log.setId(flowEventLogProvider.getNextId());
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
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
