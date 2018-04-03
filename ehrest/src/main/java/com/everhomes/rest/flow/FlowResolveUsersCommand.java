package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowResolveUsersCommand {
	Long flowId;
	Long selectionId;

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Long getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(Long selectionId) {
		this.selectionId = selectionId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
