package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型,暂时统一传PMTASK</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>moduleType: 模块类型 {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>projectId: 项目ID</li>
 * <li>projectType: 项目类型 {@link com.everhomes.rest.common.EntityType}</li>
 * <li>beginTime: 统计起始时间</li>
 * <li>endTime: 统计结束时间</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class GetEvalStatCommand {

    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private String moduleType;
    private Long beginTime;
    private Long endTime;
    private Long projectId;
    private String projectType;

    private Long appId;

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

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
