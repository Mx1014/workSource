package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class FlowScriptDTO {
    private String     scriptType;
    private String     name;
    private String     ownerType;
    private String     stepType;
    private Integer     namespaceId;
    private String     flowStepType;
    private Long     ownerId;
    private String     moduleType;
    private Long     id;
    private String     scriptCls;
    private Long     moduleId;



    public String getScriptType() {
		return scriptType;
	}



	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getOwnerType() {
		return ownerType;
	}



	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}



	public String getStepType() {
		return stepType;
	}



	public void setStepType(String stepType) {
		this.stepType = stepType;
	}



	public Integer getNamespaceId() {
		return namespaceId;
	}



	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}



	public String getFlowStepType() {
		return flowStepType;
	}



	public void setFlowStepType(String flowStepType) {
		this.flowStepType = flowStepType;
	}



	public Long getOwnerId() {
		return ownerId;
	}



	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}



	public String getModuleType() {
		return moduleType;
	}



	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getScriptCls() {
		return scriptCls;
	}



	public void setScriptCls(String scriptCls) {
		this.scriptCls = scriptCls;
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
