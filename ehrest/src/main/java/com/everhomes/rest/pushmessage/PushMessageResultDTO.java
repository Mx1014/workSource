package com.everhomes.rest.pushmessage;

import java.sql.Timestamp;

public class PushMessageResultDTO {
    private Long     id;
    private Byte     messageType;
    private String   title;
    private String   content;
    private Byte     targetType;
    private Long     targetId;
    private String   deviceType;
    private String   deviceTag;
    private String   appVersion;
    
    private Long     userId;
    private String   identifierToken;
    private Timestamp sendTime;
    
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
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getIdentifierToken() {
        return identifierToken;
    }
    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }
    public Timestamp getSendTime() {
        return sendTime;
    }
    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }
}
