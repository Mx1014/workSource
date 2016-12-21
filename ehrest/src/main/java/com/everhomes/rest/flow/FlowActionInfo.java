package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowActionInfo {
	private Byte enabled;
	private Long reminderAfterMinute;
	private Long reminderTickMinute;
	private Long trackerApplier;
	private Long trackerProcessor;
	private String renderText;
	
	CreateFlowUserSelectionCommand userSelections;

	public Long getReminderAfterMinute() {
		return reminderAfterMinute;
	}

	public void setReminderAfterMinute(Long reminderAfterMinute) {
		this.reminderAfterMinute = reminderAfterMinute;
	}

	public Long getReminderTickMinute() {
		return reminderTickMinute;
	}

	public void setReminderTickMinute(Long reminderTickMinute) {
		this.reminderTickMinute = reminderTickMinute;
	}

	public Long getTrackerApplier() {
		return trackerApplier;
	}

	public void setTrackerApplier(Long trackerApplier) {
		this.trackerApplier = trackerApplier;
	}

	public Long getTrackerProcessor() {
		return trackerProcessor;
	}

	public void setTrackerProcessor(Long trackerProcessor) {
		this.trackerProcessor = trackerProcessor;
	}

	public String getRenderText() {
		return renderText;
	}

	public void setRenderText(String renderText) {
		this.renderText = renderText;
	}
	
	public CreateFlowUserSelectionCommand getUserSelections() {
		return userSelections;
	}

	public void setUserSelections(CreateFlowUserSelectionCommand userSelections) {
		this.userSelections = userSelections;
	}

	public Byte getEnabled() {
		return enabled;
	}

	public void setEnabled(Byte enabled) {
		this.enabled = enabled;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
