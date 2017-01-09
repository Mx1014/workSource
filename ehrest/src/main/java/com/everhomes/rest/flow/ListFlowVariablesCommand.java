package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * 获取变量列表
 * <ul>
 * <li>flowVariableType {@link com.everhomes.rest.flow.FlowVariableType}</li>
 * </ul>
 * @author janson
 *
 */
public class ListFlowVariablesCommand {
    private Integer     namespaceId;
    private String flowVariableType;
    private String entityType;
    private Long entityId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getFlowVariableType() {
		return flowVariableType;
	}

	public void setFlowVariableType(String flowVariableType) {
		this.flowVariableType = flowVariableType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
