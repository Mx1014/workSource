package com.everhomes.rest.community_approve;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>ownerType: 目前默认是： EhOrganizations</li>
 * <li>ownerId: 目前是 organizationId </li>
 * <li>moduleId: 模块id</li>
 * <li>moduleType: 模块类型,模块类型 默认"any-module",参考 {@link com.everhomes.rest.flow.FlowModuleType} </li>
 * <li>orgnizationId:属于的公司</li>
 * <li>timeStart:搜索起始时间</li>
 * <li>timeEnd:搜索结束时间</li>
 * <li>approveName:审批名称</li>
 * <li>keyWord:关键字</li>
 * </ul>
 *
 */
public class ListCommunityApproveValCommand {
    private Long     ownerId;
    private String     ownerType;
    private Long     moduleId;
    private String     moduleType;
    private Long     organizationId;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private String approveName;
    private String keyWord;
    private Long pageAnchor;
    private Integer pageSize;

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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
}
