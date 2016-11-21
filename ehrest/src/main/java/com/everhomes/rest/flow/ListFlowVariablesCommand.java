package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListFlowVariablesCommand {
    private Integer     namespaceId;
    private Long     ownerId;
    private String     ownerType;
    private String     moduleType;
    private Long     moduleId;
    
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
