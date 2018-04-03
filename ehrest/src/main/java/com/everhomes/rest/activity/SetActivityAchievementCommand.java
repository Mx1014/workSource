package com.everhomes.rest.activity;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>activityId: 活动id</li>
 *     <li>achievement: 活动成果内容</li>
 *     <li>achievementType: 活动成果文本类型 richtext：富文本, link：第三方链接</li>
 *     <li>achievementRichtextUrl: 活动成果富文本页面url</li>
 * </ul>
 */
public class SetActivityAchievementCommand {

    private Long activityId;

    private String achievement;

    private String achievementType;

    private String achievementRichtextUrl;

    public String getAchievementRichtextUrl() {
        return achievementRichtextUrl;
    }

    public void setAchievementRichtextUrl(String achievementRichtextUrl) {
        this.achievementRichtextUrl = achievementRichtextUrl;
    }

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
