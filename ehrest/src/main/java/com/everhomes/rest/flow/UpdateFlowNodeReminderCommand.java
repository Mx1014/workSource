package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeReminderCommand {
	private Byte reminderMessageEnabled;
	private Byte reminderSMSEnabled;
	private Byte reminderTickMsgEnabled;
	private Byte reminderTickSMSEnabled;
	private FlowActionInfo messageAction;
	private FlowActionInfo smsAction;
	private FlowActionInfo tickMessageAction;
	private FlowActionInfo tickSMSAction;
	public Byte getReminderMessageEnabled() {
		return reminderMessageEnabled;
	}
	public void setReminderMessageEnabled(Byte reminderMessageEnabled) {
		this.reminderMessageEnabled = reminderMessageEnabled;
	}
	public Byte getReminderSMSEnabled() {
		return reminderSMSEnabled;
	}
	public void setReminderSMSEnabled(Byte reminderSMSEnabled) {
		this.reminderSMSEnabled = reminderSMSEnabled;
	}
	public Byte getReminderTickMsgEnabled() {
		return reminderTickMsgEnabled;
	}
	public void setReminderTickMsgEnabled(Byte reminderTickMsgEnabled) {
		this.reminderTickMsgEnabled = reminderTickMsgEnabled;
	}
	public Byte getReminderTickSMSEnabled() {
		return reminderTickSMSEnabled;
	}
	public void setReminderTickSMSEnabled(Byte reminderTickSMSEnabled) {
		this.reminderTickSMSEnabled = reminderTickSMSEnabled;
	}
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
