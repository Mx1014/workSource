package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

public class ListFlowCommand {
    private Integer     namespaceId;
    private Long     ownerId;
    private String     ownerType;
    private String     moduleType;
    private Long     moduleId;
    private Long flowMainId;
    private Integer flowVersion;
    private Long pageAnchor;
    private Integer pageSize;
    
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
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(Integer flowVersion) {
		this.flowVersion = flowVersion;
	}
	public Long getFlowMainId() {
		return flowMainId;
	}
	public void setFlowMainId(Long flowMainId) {
		this.flowMainId = flowMainId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
