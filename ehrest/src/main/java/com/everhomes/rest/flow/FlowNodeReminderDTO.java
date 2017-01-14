package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowNodeReminderDTO {
	private Byte reminderMessageEnabled;
	private Byte reminderSMSEnabled;
	private Byte reminderTickMsgEnabled;
	private Byte reminderTickSMSEnabled;
	private FlowActionDTO messageAction;
	private FlowActionDTO smsAction;
	private FlowActionDTO tickMessageAction;
	private FlowActionDTO tickSMSAction;

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

	public FlowActionDTO getMessageAction() {
		return messageAction;
	}

	public void setMessageAction(FlowActionDTO messageAction) {
		this.messageAction = messageAction;
	}

	public FlowActionDTO getSmsAction() {
		return smsAction;
	}

	public void setSmsAction(FlowActionDTO smsAction) {
		this.smsAction = smsAction;
	}

	public FlowActionDTO getTickMessageAction() {
		return tickMessageAction;
	}

	public void setTickMessageAction(FlowActionDTO tickMessageAction) {
		this.tickMessageAction = tickMessageAction;
	}

	public FlowActionDTO getTickSMSAction() {
		return tickSMSAction;
	}

	public void setTickSMSAction(FlowActionDTO tickSMSAction) {
		this.tickSMSAction = tickSMSAction;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
