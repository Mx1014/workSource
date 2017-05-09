package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>startTime: 起始月份  </li>
 *  <li>endTime: 截止月份  </li>
 *  <li>equipmentId: 设备id</li>
 *  <li>standardId: 标准id</li>
 * </ul>
 * Created by ying.xiong on 2017/4/18.
 */
public class StatItemResultsInEquipmentTasksCommand {

    private Long equipmentId;

    private Long standardId;

    private Long startTime;

    private Long endTime;

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
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
