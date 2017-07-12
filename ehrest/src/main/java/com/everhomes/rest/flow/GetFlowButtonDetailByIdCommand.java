package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class GetFlowButtonDetailByIdCommand {
	private Long flowButtonId;

	public Long getFlowButtonId() {
		return flowButtonId;
	}

	public void setFlowButtonId(Long flowButtonId) {
		this.flowButtonId = flowButtonId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
