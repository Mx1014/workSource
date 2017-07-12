package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowActions;
import com.everhomes.util.StringHelper;

public class FlowAction extends EhFlowActions {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3805976892366556884L;
	
	public Long getScriptId() {
		return FlowActionCustomField.SCRIPT_ID.getIntegralValue(this);
	}

	public void setScriptId(Long scriptId) {
		FlowActionCustomField.SCRIPT_ID.setIntegralValue(this, scriptId);
	}

	public Long getReminderAfterMinute() {
		return FlowActionCustomField.REMINDER_AFTER_MINUTE.getIntegralValue(this);
	}

	public void setReminderAfterMinute(Long reminderAfterMinute) {
		FlowActionCustomField.REMINDER_AFTER_MINUTE.setIntegralValue(this, reminderAfterMinute);
	}

	public Long getReminderTickMinute() {
		return FlowActionCustomField.REMINDER_TICK_MINUTE.getIntegralValue(this);
	}

	public void setReminderTickMinute(Long reminderTickMinute) {
		FlowActionCustomField.REMINDER_TICK_MINUTE.setIntegralValue(this, reminderTickMinute);
	}

	public Long getTrackerProcessor() {
		return FlowActionCustomField.TRACKER_PROCESSOR.getIntegralValue(this);
	}

	public void setTrackerProcessor(Long trackerProcessor) {
		FlowActionCustomField.TRACKER_PROCESSOR.setIntegralValue(this, trackerProcessor);
	}

	public Long getTrackerApplier() {
		return FlowActionCustomField.TRACKER_APLIER.getIntegralValue(this);
	}

	public void setTrackerApplier(Long trackerApplier) {
		FlowActionCustomField.TRACKER_APLIER.setIntegralValue(this, trackerApplier);
	}

	public Long getAllowTimeoutAction() {
		return FlowActionCustomField.ALLOW_TIMEOUT_ACTION.getIntegralValue(this);
	}

	public void setAllowTimeoutAction(Long allowTimeoutAction) {
		FlowActionCustomField.ALLOW_TIMEOUT_ACTION.setIntegralValue(this, allowTimeoutAction); 
	}

	public Long getTemplateId() {
		return FlowActionCustomField.TEMPLATE_ID.getIntegralValue(this);
	}

	public void setTemplateId(Long templateId) {
		FlowActionCustomField.TEMPLATE_ID.setIntegralValue(this, templateId);
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
