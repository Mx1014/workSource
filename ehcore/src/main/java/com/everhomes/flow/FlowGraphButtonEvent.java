package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.RuntimeErrorException;

public class FlowGraphButtonEvent implements FlowGraphEvent {
	private FlowUserType userType;
	private UserInfo firedUser;
	private FlowFireButtonCommand cmd;
	private FlowSubject subject;
	
	private FlowEventLogProvider flowEventLogProvider;
	
	public FlowGraphButtonEvent() {
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
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
		
		FlowEventLog log = null;
		FlowCase flowCase = ctx.getFlowCase();
		
		//current state change to next step
		FlowGraphNode current = ctx.getCurrentNode();
		FlowGraphNode next = null;
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
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		case REJECT_STEP:
			if(current.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
			}
			next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()-1);
			ctx.setNextNode(next);
			
			flowCase.setRejectNodeId(current.getFlowNode().getId());
			flowCase.setRejectCount(flowCase.getRejectCount() + 1);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		case TRANSFER_STEP:
			//TODO processor changed, add a log
			break;
		case COMMENT_STEP:
			next = current;
			ctx.setNextNode(next);
			
			log = new FlowEventLog();
			log.setId(flowEventLogProvider.getNextId());
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			log.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
			log.setParentId(0l);
			log.setFlowCaseId(ctx.getFlowCase().getId());
			log.setFlowUserId(ctx.getOperator().getId());
			log.setFlowUserName(ctx.getOperator().getNickName());
			log.setLogContent(subject.getContent());
			log.setStepCount(ctx.getFlowCase().getStepCount());
			log.setLogType(FlowLogType.NODE_TRACKER.getCode());
			log.setSubjectId(subject.getId());
			log.setButtonFiredStep(nextStep.getCode());
			log.setTrackerApplier(1l);
			log.setTrackerProcessor(1l);
			ctx.getLogs().add(log);
			
			break;
		case ABSORT_STEP:
			next = ctx.getFlowGraph().getNodes().get(ctx.getFlowGraph().getNodes().size()-1);
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		case REMINDER_STEP:
			next = current;
			ctx.setNextNode(next);
			break;
		case EVALUATE_STEP:
			//TODO save evaluate score
			break;
		default:
			break;
		}
		
		log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
		log.setFlowButtonId(btn.getFlowButton().getId());
		log.setFlowNodeId(next.getFlowNode().getId());
		log.setParentId(0l);
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(firedUser.getId());
		log.setFlowUserName(firedUser.getNickName());
		if(FlowEntityType.FLOW_SELECTION.getCode().equals(cmd.getFlowEntityType())) {
			log.setFlowSelectionId(cmd.getEntityId());
		}
		log.setSubjectId(subject.getId());
		log.setLogType(FlowLogType.BUTTON_FIRED.getCode());
		log.setButtonFiredStep(nextStep.getCode());
		log.setButtonFiredFromNode(current.getFlowNode().getId());
		ctx.getLogs().add(log);	//added but not save to database now.
	}

	@Override
	public Long getFiredButtonId() {
		return cmd.getButtonId();
	}
}
