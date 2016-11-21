package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class GetFlowCaseDetailByIdCommand {
	private Long flowCaseId;

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
