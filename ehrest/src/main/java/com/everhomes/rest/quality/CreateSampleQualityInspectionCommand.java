package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>name: 检查名称</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>communityIds: 关联小区id列表</li>
 *     <li>executeGroupAndPositionList:执行组部门岗位id列表 参考{@link com.everhomes.rest.quality.ExecuteGroupAndPosition}</li>
 * </ul>
 * Created by ying.xiong on 2017/6/1.
 */
public class CreateSampleQualityInspectionCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private String name;

    private Long startTime;

    private Long endTime;

    @ItemType(Long.class)
    private List<Long> communityIds;

    @ItemType(ExecuteGroupAndPosition.class)
    private List<ExecuteGroupAndPosition> executeGroupAndPositionList;

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<ExecuteGroupAndPosition> getExecuteGroupAndPositionList() {
        return executeGroupAndPositionList;
    }

    public void setExecuteGroupAndPositionList(List<ExecuteGroupAndPosition> executeGroupAndPositionList) {
        this.executeGroupAndPositionList = executeGroupAndPositionList;
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
