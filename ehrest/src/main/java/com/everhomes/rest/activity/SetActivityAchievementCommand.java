package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activityId: 活动id</li>
 *     <li>achievement: 活动成果富文本</li>
 * </ul>
 */
public class SetActivityAchievementCommand {

    private Long activityId;

    private String achievement;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
