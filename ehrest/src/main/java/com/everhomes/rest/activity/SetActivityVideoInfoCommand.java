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
    private String roomId;
    private String vid;
    private Byte isContinue;

    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getVid() {
        return vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public Long getActivityId() {
        return activityId;
    }
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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
