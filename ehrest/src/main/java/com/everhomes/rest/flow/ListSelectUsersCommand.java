package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowCaseId: 工作流 case Id</li>
 * <li>entityId: 用户选择项的 ID</li>
 * <li>flowEntityType: {@link com.everhomes.rest.flow.FlowEntityType}</li>
 * <li>flowUserType: {@link com.everhomes.rest.flow.FlowUserType}</li>
 * </ul>
 * @author janson
 *
 */
public class ListSelectUsersCommand {
	private Long flowCaseId;
	private Long entityId;
	private String flowEntityType;
	private String flowUserType;

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getFlowEntityType() {
		return flowEntityType;
	}

	public void setFlowEntityType(String flowEntityType) {
		this.flowEntityType = flowEntityType;
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
