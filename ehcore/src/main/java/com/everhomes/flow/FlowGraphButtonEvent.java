package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;

public class FlowGraphButtonEvent implements FlowGraphEvent {
	private FlowUserType userType;
	private Long firedUserId;
	private FlowFireButtonCommand cmd;
	
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
		return firedUserId;
	}

	public void setUserType(FlowUserType userType) {
		this.userType = userType;
	}

	public void setFiredUserId(Long firedUserId) {
		this.firedUserId = firedUserId;
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
		
		//current state change to next step
		switch(nextStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			ctx.getCurrentEvent();
			FlowGraphNode current = ctx.getCurrentNode();
			FlowGraphNode next = null;
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
			break;
		case TRANSFER_STEP:
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
