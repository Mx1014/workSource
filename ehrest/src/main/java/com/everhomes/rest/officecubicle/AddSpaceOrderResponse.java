package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowCaseId: 工作流Id</li> 
 * </ul>
 */
public class AddSpaceOrderResponse {	
	private Long flowCaseId;

	public AddSpaceOrderResponse(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public AddSpaceOrderResponse() {
	}

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
