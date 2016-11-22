package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * 创建新的工作流。
 * <ul>
 * <li>flowName: 工作流下的 flowName 在某一个业务下是独一无二的。</li>
 * <li>ownerType: {@link com.everhomes.rest.flow.FlowOwnerType} </li>
 * </ul>
 * @author janson
 *
 */
public class CreateFlowCommand {
    private String     flowName;
    private Integer     namespaceId;
    private Long     ownerId;
    private String     ownerType;
    private String     moduleType;
    private Long     moduleId;
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
