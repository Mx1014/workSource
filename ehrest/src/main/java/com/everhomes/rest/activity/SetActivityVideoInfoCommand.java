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

    private Integer namespaceId;
    private Long activityId;
    private String roomId;
    private String vid;
    private Byte isContinue;
    private Byte state;

    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
