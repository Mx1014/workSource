package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entryId: 入口id</li>
 *     <li>entryName: 入口名称</li>
 *     <li>activityId: 活动id</li>
 *     <li>activityName: 活动名称</li>
 * </ul>
 */
public class BannerActivityTargetData {

    private Long entryId;
    private String entryName;
    private Long activityId;
    private String activityName;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
