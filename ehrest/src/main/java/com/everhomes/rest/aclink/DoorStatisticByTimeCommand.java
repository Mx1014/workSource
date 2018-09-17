// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>ownerId：所属机构Id</li>
 * <li>ownerType：所属机构类型 0小区1企业2家庭</li>
 * <li>startTime: 查询开始日期</li>
 * <li>endTime: 查询结束日期</li>
 * <li>currentPMId: 当前管理公司ID(organizationID)</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class DoorStatisticByTimeCommand {
    private Long ownerId;

    private Byte ownerType;

    private Integer pageSize;

    private Long startTime;

    private Long endTime;
    //权限
    private Long appId;
    private Long currentPMId;
    private Long currentProjectId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
