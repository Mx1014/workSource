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
    private Long orgId;
    private Long     moduleId;
    private String projectType;
    private Long projectId;
    private String stringTag1;
    private String stringTag2;
    private Long integralTag1;
    private Long integralTag2;
    
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

	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStringTag1() {
		return stringTag1;
	}
	public void setStringTag1(String stringTag1) {
		this.stringTag1 = stringTag1;
	}
	public String getStringTag2() {
		return stringTag2;
	}
	public void setStringTag2(String stringTag2) {
		this.stringTag2 = stringTag2;
	}
	public Long getIntegralTag1() {
		return integralTag1;
	}
	public void setIntegralTag1(Long integralTag1) {
		this.integralTag1 = integralTag1;
	}
	public Long getIntegralTag2() {
		return integralTag2;
	}
	public void setIntegralTag2(Long integralTag2) {
		this.integralTag2 = integralTag2;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
