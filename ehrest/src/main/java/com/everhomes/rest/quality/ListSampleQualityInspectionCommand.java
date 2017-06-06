package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>name: 检查名称</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>communityId: 小区id</li>
 * </ul>
 * Created by ying.xiong on 2017/6/1.
 */
public class ListSampleQualityInspectionCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Long communityId;

    private Long startTime;

    private Long endTime;

    private String name;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
