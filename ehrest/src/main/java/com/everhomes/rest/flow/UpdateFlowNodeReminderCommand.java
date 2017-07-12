package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeReminderCommand {
	private Long flowNodeId;
	private FlowActionInfo messageAction;
	private FlowActionInfo smsAction;
	private FlowActionInfo tickMessageAction;
	private FlowActionInfo tickSMSAction;
	
	public FlowActionInfo getMessageAction() {
		return messageAction;
	}
	public void setMessageAction(FlowActionInfo messageAction) {
		this.messageAction = messageAction;
	}
	public FlowActionInfo getSmsAction() {
		return smsAction;
	}
	public void setSmsAction(FlowActionInfo smsAction) {
		this.smsAction = smsAction;
	}
	public FlowActionInfo getTickMessageAction() {
		return tickMessageAction;
	}
	public void setTickMessageAction(FlowActionInfo tickMessageAction) {
		this.tickMessageAction = tickMessageAction;
	}
	public FlowActionInfo getTickSMSAction() {
		return tickSMSAction;
	}
	public void setTickSMSAction(FlowActionInfo tickSMSAction) {
		this.tickSMSAction = tickSMSAction;
	}

	public Long getFlowNodeId() {
		return flowNodeId;
	}
	public void setFlowNodeId(Long flowNodeId) {
		this.flowNodeId = flowNodeId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
