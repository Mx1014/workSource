package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 任务所属公司id</li>
 *  <li>ownerType: 任务所属公司类型</li>
 *  <li>targetId: 任务所属项目等的id</li>
 *  <li>targetType: 任务所属项目类型，如community</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>startTime: 起始月份  </li>
 *  <li>endTime: 截止月份  </li>
 * </ul>
 * Created by ying.xiong on 2017/4/18.
 */
public class StatIntervalAllEquipmentTasksCommand {

    private Long ownerId;

    private String ownerType;

    private Long targetId;

    private String targetType;

    private Long inspectionCategoryId;

    private Long startTime;

    private Long endTime;

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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getInspectionCategoryId() {
        return inspectionCategoryId;
    }

    public void setInspectionCategoryId(Long inspectionCategoryId) {
        this.inspectionCategoryId = inspectionCategoryId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
