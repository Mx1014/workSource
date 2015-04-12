package com.everhomes.activity;

import com.everhomes.util.StringHelper;

public class ActivityCheckinCommand {
    private Long activityId;

    public ActivityCheckinCommand() {
    }

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
