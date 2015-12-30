package com.everhomes.rest.pushmessage;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: The result of message Id</li>
 * <li>messageType: {@link com.everhomes.pushmessage.PushMessageType} VERSION_UPGRADE((byte)2), NOTIFY((byte)1), NORMAL((byte)0)</li>
 * <li>targetType: {@link com.everhomes.pushmessage.PushMessageTargetType} CITY((byte)3), COMMUNITY((byte)2), FAMILY((byte)1), USER((byte)0)</li>
 * <li>status: {@link com.everhomes.pushmessage.PushMessageStatus} Processing(2), Finished(1), Ready(0)</li>
 * <li>deviceType: iOS/Android</li>
 * <li>startTime: The start for pushing job, if null execute redirectly</li>
 * </ul>
 * @author janson
 *
 */
public class PushMessageDTO {
    private Long     id;
    private Byte     messageType;
    private String   title;
    private String   content;
    private Byte     targetType;
    private Long     targetId;
    private Integer  status;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp finishTime;
    private String   deviceType;
    private String   deviceTag;
    private String   appVersion;
    private Long     pushCount;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Byte getMessageType() {
        return messageType;
    }
    public void setMessageType(Byte messageType) {
        this.messageType = messageType;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Byte getTargetType() {
        return targetType;
    }
    public void setTargetType(Byte targetType) {
        this.targetType = targetType;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    public Timestamp getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getDeviceTag() {
        return deviceTag;
    }
    public void setDeviceTag(String deviceTag) {
        this.deviceTag = deviceTag;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    public Long getPushCount() {
        return pushCount;
    }
    public void setPushCount(Long pushCount) {
        this.pushCount = pushCount;
    }
    
    
}
