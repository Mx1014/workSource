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

	@Override
	public void fire(FlowCaseState ctx) {
		//TODO create logs
		FlowGraphButton btn = ctx.getFlowGraph().getGraphButton(cmd.getButtonId());
		FlowStepType nextStep = FlowStepType.fromCode(btn.getFlowButton().getFlowStepType());
		ctx.setStepType(nextStep);
		
		//current state change to next step
		FlowGraphNode current = null;
		FlowGraphNode next = null;
		switch(nextStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			current = ctx.getCurrentNode();
			next = null;
			if(!btn.getFlowButton().getGotoNodeId().equals(0)) {
				next = ctx.getFlowGraph().getGraphNode(btn.getFlowButton().getGotoNodeId());
			}
			if(next == null) {
				//get next level
				next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()+1);
			}
			ctx.setNextNode(next);
			break;
		case REJECT_STEP:
			current = ctx.getCurrentNode();
			if(current.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
			}
			next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()-1);
			ctx.setNextNode(next);
			break;
		case TRANSFER_STEP:
			//TODO processor changed, add a log
			break;
		case COMMENT_STEP:
			break;
		case ABSORT_STEP:
			next = ctx.getFlowGraph().getNodes().get(ctx.getFlowGraph().getNodes().size()-1);
			ctx.setNextNode(next);
			break;
		case REMINDER_STEP:
			//TODO resend a message
			break;
		case EVALUATE_STEP:
			//TODO save evaluate score
			break;
		default:
			break;
		}
		
		FlowEventLog log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getModuleId());
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
		
		log.setLogType(FlowLogType.BUTTON_FIRED.getCode());
		log.setLogTitle("");
		log.setButtonFiredStep(nextStep.getCode());
		log.setButtonFiredFromNode(current.getFlowNode().getId());
		ctx.getLogs().add(log);	//added but not save to database now.
	}

	@Override
	public Long getFiredButtonId() {
		return cmd.getButtonId();
	}
}
