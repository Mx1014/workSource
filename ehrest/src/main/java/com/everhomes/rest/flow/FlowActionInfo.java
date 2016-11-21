package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowActionInfo {
	private String renderText;
	
	CreateFlowUserSelectionCommand userSelections;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
