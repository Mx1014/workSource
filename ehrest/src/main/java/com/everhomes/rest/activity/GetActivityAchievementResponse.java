package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * achievement: 活动成果
 */
public class GetActivityAchievementResponse {

    private String achievement;

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
