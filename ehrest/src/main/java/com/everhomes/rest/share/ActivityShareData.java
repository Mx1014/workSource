package com.everhomes.rest.share;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activityId: activityId</li>
 * </ul>
 */
public class ActivityShareData {

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
