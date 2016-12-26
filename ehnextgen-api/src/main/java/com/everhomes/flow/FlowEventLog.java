package com.everhomes.flow;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.util.StringHelper;

public class FlowEventLog extends EhFlowEventLogs {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6063163152705295466L;

	public Long getButtonFiredFromNode() {
		return FlowEventCustomField.BUTTON_FIRED_FROM_NODE.getIntegralValue(this);
	}

	public void setButtonFiredFromNode(Long buttonFiredFromNode) {
		FlowEventCustomField.BUTTON_FIRED_FROM_NODE.setIntegralValue(this, buttonFiredFromNode);
	}

	public String getButtonFiredStep() {
		return FlowEventCustomField.BUTTON_FIRED_STEP.getStringValue(this);
	}

	public void setButtonFiredStep(String buttonFiredStep) {
		FlowEventCustomField.BUTTON_FIRED_STEP.setStringValue(this, buttonFiredStep);
	}

	public Long getTrackerProcessor() {
		return FlowEventCustomField.TRACKER_PROCESSOR.getIntegralValue(this);
	}

	public void setTrackerProcessor(Long trackerProcessor) {
		FlowEventCustomField.TRACKER_PROCESSOR.setIntegralValue(this, trackerProcessor);
	}

	public Long getTrackerApplier() {
		return FlowEventCustomField.TRACKER_APLIER.getIntegralValue(this);
	}

	public void setTrackerApplier(Long trackerApplier) {
		FlowEventCustomField.TRACKER_APLIER.setIntegralValue(this, trackerApplier);
	}

	public Long getButtonFiredCount() {
		return FlowEventCustomField.BUTTON_FIRED_COUNT.getIntegralValue(this);
	}

	public void setButtonFiredCount(Long buttonFiredCount) {
		FlowEventCustomField.BUTTON_FIRED_COUNT.setIntegralValue(this, buttonFiredCount);
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
