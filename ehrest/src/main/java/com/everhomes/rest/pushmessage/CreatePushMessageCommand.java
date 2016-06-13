package com.everhomes.rest.pushmessage;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

/**
 * <ul> 创建推送消息
 * <li>title: 推送标题</li>
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
public class CreatePushMessageCommand {
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
    
    private Long startTime;
    
    private String   deviceType;
    private String   deviceTag;
    private String   appVersion;
    
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
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
    
    
}
