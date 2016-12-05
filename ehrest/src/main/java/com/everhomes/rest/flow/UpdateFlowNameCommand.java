package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class UpdateFlowNameCommand {
	private Long flowId;
	private String newFlowName;
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public String getNewFlowName() {
		return newFlowName;
	}
	public void setNewFlowName(String newFlowName) {
		this.newFlowName = newFlowName;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
