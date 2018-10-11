package com.everhomes.rest.flow;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>ownerId: ownerId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>moduleType: moduleType</li>
 *     <li>moduleId: moduleId</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>projectId: 项目 ID</li>
 *     <li>projectType: projectType</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 *     <li>snapshotFlag: snapshotFlag</li>
 *     <li>orgId: 多管理公司id</li>
 * </ul>
 */
public class ListFlowCommand {

    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private String moduleType;
    private Long moduleId;
    private Long flowMainId;
    private Integer flowVersion;
    private Long projectId;
    private String projectType;
    private Long pageAnchor;
    private Integer pageSize;
    private Byte snapshotFlag;
    @NotNull
	private Long orgId;

    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

    public Byte getSnapshotFlag() {
        return snapshotFlag;
    }

    public void setSnapshotFlag(Byte snapshotFlag) {
        this.snapshotFlag = snapshotFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
