// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>ownerId：所属机构Id</li>
 * <li>ownerType：所属机构类型 0小区1企业2家庭</li>
 * <li>startTime: 查询开始日期</li>
 * <li>endTime: 查询结束日期</li>
 * </ul>
 */
public class DoorStatisticByTimeCommand {
    private Long ownerId;

    private Byte ownerType;

    private Integer pageSize;

    private Long startTime;

    private Long endTime;

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
