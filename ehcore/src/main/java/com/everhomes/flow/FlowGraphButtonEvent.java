package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraphButtonEvent implements FlowGraphEvent {
	private FlowUserType userType;
	private UserInfo firedUser;
	private FlowFireButtonCommand cmd;
	private FlowSubject subject;
	
	private FlowEventLogProvider flowEventLogProvider;
	private UserService userService;
	private FlowService flowService;
	
	public FlowGraphButtonEvent() {
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
		userService = PlatformContext.getComponent(UserService.class);
		flowService = PlatformContext.getComponent(FlowService.class);
//		flowButtonProvider = PlatformContext.getComponent(FlowButtonProvider.class);
	}
	
	@Override
	public FlowUserType getUserType() {
		return userType;
	}

	@Override
	public FlowEventType getEventType() {
		return FlowEventType.BUTTON_FIRED;
	}

	@Override
	public Long getFiredUserId() {
		return firedUser.getId();
	}

	public UserInfo getFiredUser() {
		return firedUser;
	}

	public void setFiredUser(UserInfo firedUser) {
		this.firedUser = firedUser;
	}

	public void setUserType(FlowUserType userType) {
		this.userType = userType;
	}

	public FlowFireButtonCommand getCmd() {
		return cmd;
	}

	public void setCmd(FlowFireButtonCommand cmd) {
		this.cmd = cmd;
	}

	public FlowSubject getSubject() {
		return subject;
	}

	public void setSubject(FlowSubject subject) {
		this.subject = subject;
	}

	@Override
	public void fire(FlowCaseState ctx) {
		FlowGraphButton btn = ctx.getFlowGraph().getGraphButton(cmd.getButtonId());
		FlowStepType nextStep = FlowStepType.fromCode(btn.getFlowButton().getFlowStepType());
		ctx.setStepType(nextStep);
		
		FlowLogType logType = FlowLogType.BUTTON_FIRED;
		FlowEventLog log = null;
		FlowEventLog tracker = null;
		FlowCase flowCase = ctx.getFlowCase();
		
		//current state change to next step
		FlowGraphNode current = ctx.getCurrentNode();
		FlowGraphNode next = null;
		
		UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());
		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("nodeName", current.getFlowNode().getNodeName());
		templateMap.put("applierName", applier.getNickName());
		
		switch(nextStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			next = null;
			if(!btn.getFlowButton().getGotoNodeId().equals(0)) {
				next = ctx.getFlowGraph().getGraphNode(btn.getFlowButton().getGotoNodeId());
			}
			if(next == null) {
				//get next level
				next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()+1);
			}
			
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			
			break;
		case REJECT_STEP:
			if(current.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
			}
			
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			
			next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()-1);
			ctx.setNextNode(next);
			
			flowCase.setRejectNodeId(current.getFlowNode().getId());
			flowCase.setRejectCount(flowCase.getRejectCount() + 1);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			
			break;
		case TRANSFER_STEP:
			next = current;
			ctx.setNextNode(next);
			
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			
			log = new FlowEventLog();
			log.setId(flowEventLogProvider.getNextId());
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());//get real version
			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			if(ctx.getCurrentEvent() != null) {
				log.setFlowButtonId(ctx.getCurrentEvent().getFiredButtonId());	
			}
			log.setFlowNodeId(next.getFlowNode().getId());
			log.setParentId(0l);
			log.setFlowCaseId(ctx.getFlowCase().getId());
			log.setFlowUserId(cmd.getEntityId());
			log.setStepCount(ctx.getFlowCase().getStepCount());
			log.setLogType(FlowLogType.NODE_ENTER.getCode());
			log.setLogTitle("");
			ctx.getLogs().add(log);
			
			break;
		case COMMENT_STEP:
			next = current;
			ctx.setNextNode(next);
			
			if(subject == null) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow node step param error");
			}
			
			tracker = new FlowEventLog();
			if(subject != null) {
				tracker.setLogContent(subject.getContent());	
			} else {
				templateMap.put("imageCount", String.valueOf(cmd.getImages().size()));
				tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			}
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			
			break;
		case ABSORT_STEP:
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			
			next = ctx.getFlowGraph().getNodes().get(ctx.getFlowGraph().getNodes().size()-1);
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			
			break;
		case REMINDER_STEP:
			next = current;
			ctx.setNextNode(next);
			
			logType = FlowLogType.NODE_REMIND;
			log = new FlowEventLog();
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			log.setFlowCaseId(ctx.getFlowCase().getId());
			log.setFlowUserId(firedUser.getId());
			log.setLogType(logType.getCode());
			log.setFlowNodeId(next.getFlowNode().getId());
			List<FlowEventLog> remindLogs = flowEventLogProvider.findFiredEventsByLog(log);
			
			if(!btn.getFlowButton().getRemindCount().equals(0l) 
					&& remindLogs != null 
					&& remindLogs.size() > btn.getFlowButton().getRemindCount().intValue()) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_REMIND_ERROR, "remind count overflow");
			}
			
			tracker = null;
			
			break;
		case EVALUATE_STEP:
			//TODO save evaluate score
			break;
		default:
			break;
		}
		
		if(tracker != null && subject != null) {
			tracker.setId(flowEventLogProvider.getNextId());
			tracker.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			tracker.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			tracker.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
			tracker.setParentId(0l);
			tracker.setFlowCaseId(ctx.getFlowCase().getId());
			tracker.setFlowUserId(ctx.getOperator().getId());
			tracker.setFlowUserName(ctx.getOperator().getNickName());
			tracker.setSubjectId(subject.getId());
			tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());
			
			tracker.setButtonFiredStep(nextStep.getCode());
			tracker.setTrackerApplier(1l);
			tracker.setTrackerProcessor(1l);	
			ctx.getLogs().add(tracker);
		}
		
		log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(firedUser.getId());
		log.setLogType(logType.getCode());
        log.setButtonFiredCount(0L);
		
		//Important!!! not change this order
		if(logType == FlowLogType.BUTTON_FIRED) {
			List<FlowEventLog> likeLogs = flowEventLogProvider.findFiredEventsByLog(log);
			if(likeLogs != null && likeLogs.size() > 0) {
				log.setButtonFiredCount(new Long(likeLogs.size()));
			}	
		}
		
		log.setFlowButtonId(btn.getFlowButton().getId());
		log.setFlowNodeId(next.getFlowNode().getId());
		log.setParentId(0l);
		log.setFlowUserName(firedUser.getNickName());
		if(FlowEntityType.FLOW_SELECTION.getCode().equals(cmd.getFlowEntityType())) {
			log.setFlowSelectionId(cmd.getEntityId());
		}
		log.setSubjectId(subject.getId());
		log.setButtonFiredStep(nextStep.getCode());
		log.setButtonFiredFromNode(current.getFlowNode().getId());
		
		ctx.getLogs().add(log);	//added but not save to database now.

	}

	@Override
	public Long getFiredButtonId() {
		return cmd.getButtonId();
	}
}
