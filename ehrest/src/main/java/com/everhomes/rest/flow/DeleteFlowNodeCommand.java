package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class DeleteFlowNodeCommand {
	private Long flowNodeId;

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
