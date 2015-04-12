// @formatter:off
package com.everhomes.activity;

import com.everhomes.util.StringHelper;

public class ActivitySignupCommand {
    private Long activityId;
    
    private Integer adultCount;
    private Integer childCount;
    
    public ActivitySignupCommand() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
