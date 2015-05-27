package com.everhomes.messaging;

import java.util.Date;
import java.util.Map;

import com.everhomes.discover.ItemType;

/**
 * 设备推送消息
 * <ul>
 * <li>messageId:</li>
 * <li>alert:</li>
 * <li>title:</li>
 * <li>icon:</li>
 * <li>audio:</li>
 * <li>alertType:</li>
 * <li><p>createTime:记录创建时间</p></li>
 * <li><p>timeLive:</p></li>
 * <li>extra:</li>
 * </ul>
 * @author janson
 *
 */
public class DeviceMessage {
    private Long messageId;
    private String alert;
    private String title;
    private String icon;
    private String audio;
    private String alertType;
    private Date createTime;
    private Long timeLive;
    
    @ItemType(String.class)
    private Map<String, String> extra;
    
    public Long getMessageId() {
        return messageId;
    }
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
    public String getAlert() {
        return alert;
    }
    public void setAlert(String alert) {
        this.alert = alert;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getAudio() {
        return audio;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }
    public String getAlertType() {
        return alertType;
    }
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }
    public Map<String, String> getExtra() {
        return extra;
    }
    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Long getTimeLive() {
        return timeLive;
    }
    public void setTimeLive(Long timeLive) {
        this.timeLive = timeLive;
    }
    
}
