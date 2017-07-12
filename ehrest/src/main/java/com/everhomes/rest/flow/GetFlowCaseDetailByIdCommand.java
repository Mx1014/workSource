package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowUserType: {@link com.everhomes.rest.flow.FlowUserType} processor: 处理者， node_applier: 申请者</li>
 * </ul>
 * @author janson
 *
 */
public class GetFlowCaseDetailByIdCommand {
	private Long flowCaseId;
	private String flowUserType;

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public String getFlowUserType() {
		return flowUserType;
	}

	public void setFlowUserType(String flowUserType) {
		this.flowUserType = flowUserType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
