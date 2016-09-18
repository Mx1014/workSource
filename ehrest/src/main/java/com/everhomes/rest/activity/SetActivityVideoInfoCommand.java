package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>activityId: 活动帖子 ID</li>
 * </ul>
 * @author janson
 *
 */
public class SetActivityVideoInfoCommand {
    private Long activityId;
    private Long channelId;
    private Byte isContinue;

    public Long getActivityId() {
        return activityId;
    }
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public Byte getIsContinue() {
        return isContinue;
    }
    public void setIsContinue(Byte isContinue) {
        this.isContinue = isContinue;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
