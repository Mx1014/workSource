package com.everhomes.rest.pushmessage;

import javax.validation.constraints.NotNull;

/**
 * 修改为执行的推送消息，修改之后，之前的条目将作废。
 * @author janson
 *
 */
public class UpdatePushMessageCommand {
    @NotNull
    private Long id;
    
    @NotNull
    private Byte     messageType;
    
    @NotNull
    private String   title;
    
    @NotNull
    private String   content;
    
    @NotNull
    private Byte     targetType;
    
    @NotNull
    private Long     targetId;
    
    private String   deviceType;
    private String   deviceTag;
    private String   appVersion;
    
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
    
    
}
