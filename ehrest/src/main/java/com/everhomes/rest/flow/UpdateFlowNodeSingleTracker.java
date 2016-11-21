package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNodeSingleTracker {
	private Byte applierVisible;
	private Byte processorVisible;
	private String renderText;

	public Byte getApplierVisible() {
		return applierVisible;
	}

	public void setApplierVisible(Byte applierVisible) {
		this.applierVisible = applierVisible;
	}

	public Byte getProcessorVisible() {
		return processorVisible;
	}

	public void setProcessorVisible(Byte processorVisible) {
		this.processorVisible = processorVisible;
	}

	public String getRenderText() {
		return renderText;
	}

	public void setRenderText(String renderText) {
		this.renderText = renderText;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
