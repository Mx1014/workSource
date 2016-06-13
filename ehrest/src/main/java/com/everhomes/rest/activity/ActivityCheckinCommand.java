// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ActivityCheckinCommand {
    @NotNull
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
