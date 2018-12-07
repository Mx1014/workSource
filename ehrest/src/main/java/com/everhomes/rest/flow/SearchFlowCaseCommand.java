package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间 ID</li>
 *     <li>userId: 用户ID，为空则为当前用户</li>
 *     <li>organizationId: 公司id</li>
 *     <li>flowCaseStatus: 状态 {@link com.everhomes.rest.flow.FlowCaseStatus }</li>
 *     <li>moduleId: 业务模块ID，选择了 moduleId 则不需要 keyword，可为空</li>
 *     <li>ownerId: 业务实体的类型，可为空</li>
 *     <li>ownerType: ownerType</li>
 *     <li>keyword: 搜索关键字</li>
 *     <li>appId: 应用Id</li>
 *     <li>serviceType: 业务类别</li>
 *     <li>pageSize: pageSize</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>flowCaseSearchType: 0 我的申请， 1: 待办任务， 2: 已办任务， 3: 我的督办 {@link com.everhomes.rest.flow.FlowCaseSearchType}</li>
 *     <li>projectId: 项目ID</li>
 *     <li>projectType: 项目类型</li>
 *     <li>startTime: startTime</li>
 *     <li>endTime: endTime</li>
 *     <li>originAppId: 应用 Id</li>
 * </ul>
 */
public class SearchFlowCaseCommand {

    private Integer namespaceId;
    private Long userId;
    private Long organizationId;
    private Byte flowCaseStatus;
    private Long moduleId;
    private Long ownerId;
    private String ownerType;
    private String keyword;
    private Long appId;
    private String serviceType;
    private Integer pageSize;
    private Long pageAnchor;
    private Byte flowCaseSearchType;
    private Long projectId;
    private String projectType;
    private Long startTime;
    private Long endTime;
    private Long originAppId;

    public Byte getFlowCaseStatus() {
        return flowCaseStatus;
    }

    public void setFlowCaseStatus(Byte flowCaseStatus) {
        this.flowCaseStatus = flowCaseStatus;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Byte getFlowCaseSearchType() {
        return flowCaseSearchType;
    }

    public void setFlowCaseSearchType(Byte flowCaseSearchType) {
        this.flowCaseSearchType = flowCaseSearchType;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getOriginAppId() {
        return originAppId;
    }

    public void setOriginAppId(Long originAppId) {
        this.originAppId = originAppId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
