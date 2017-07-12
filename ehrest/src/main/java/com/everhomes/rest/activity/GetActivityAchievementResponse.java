package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>achievement: 活动成果内容</li>
 *     <li>achievementType: 活动成果文本类型 richtext：富文本, link：第三方链接</li>
 * </ul>
 */
public class GetActivityAchievementResponse {

    private String achievement;
    private String achievementType;

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
