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
    private Long     ownerId;
    private String     ownerType;
    private String     moduleType;
    private Long     moduleId;
    private String flowVariableType;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getFlowVariableType() {
		return flowVariableType;
	}
	public void setFlowVariableType(String flowVariableType) {
		this.flowVariableType = flowVariableType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
