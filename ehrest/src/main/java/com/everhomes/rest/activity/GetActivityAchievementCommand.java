package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * activityId：活动id
 */
public class GetActivityAchievementCommand {

    private Long activityId;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
