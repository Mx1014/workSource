package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>communityId: 小区id</li>
 *     <li>routineId: 例行检查id</li>
 * </ul>
 * Created by ying.xiong on 2017/6/2.
 */
public class ListRoutineQualityInspectionTasksCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Long communityId;

    private Long routineId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public Long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Long routineId) {
        this.routineId = routineId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
