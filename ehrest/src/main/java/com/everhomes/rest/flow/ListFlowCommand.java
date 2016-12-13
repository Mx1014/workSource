package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>projectId: 项目 ID，如果有项目 ID， 则只搜索启用的 Flows</li>
 * </ul>
 * @author janson
 *
 */
public class ListFlowCommand {
    private Integer     namespaceId;
    private Long     ownerId;
    private String     ownerType;
    private String     moduleType;
    private Long     moduleId;
    private Long flowMainId;
    private Integer flowVersion;
    private Long projectId;
    private String projectType;
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
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
