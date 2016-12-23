package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowScriptDTO {
	private Long id;
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long moduleId;
	private String moduleType;
	private String name;
	private String scriptType;
	private String scriptCls;
	private String flowStepType;
	private String stepType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getScriptCls() {
		return scriptCls;
	}

	public void setScriptCls(String scriptCls) {
		this.scriptCls = scriptCls;
	}

	public String getFlowStepType() {
		return flowStepType;
	}

	public void setFlowStepType(String flowStepType) {
		this.flowStepType = flowStepType;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
