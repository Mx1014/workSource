// @formatter:off
package com.everhomes.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ActivityListCommand {
    @NotNull
    private Long activityId;

    private Long anchor;
    
    private Integer pageSize;
    
    public ActivityListCommand() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    
    
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
