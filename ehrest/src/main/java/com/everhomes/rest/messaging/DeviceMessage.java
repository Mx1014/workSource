package com.everhomes.rest.messaging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 设备推送消息
 * <ul>
 * <li>alert:消息的文字内容</li>
 * <li>title:消息标题</li>
 * <li>icon:消息图标</li>
 * <li>audio:音频</li>
 * <li>alertType:消息类型</li>
 * <li><p>createTime:记录创建时间</p></li>
 * <li><p>timeLive:消息存活时间</p></li>
 * <li>extra:附加内容</li>
 * </ul>
 * @author janson
 *
 */
public class DeviceMessage {
    private String alert;
    private String title;
    private String icon;
    private String audio;
    private String alertType;
    private Date createTime;
    private Long timeLive;
    private String action;
    private Long appId;
    private Integer badge;
    private int priorigy;
    
    @ItemType(String.class)
    private Map<String, String> extra;
    
    public DeviceMessage() {
        extra = new HashMap<String, String>();
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
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public Long getAppId() {
        return appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    public Integer getBadge() {
        return badge;
    }
    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public int getPriorigy() {
        return priorigy;
    }

    public void setPriorigy(int priorigy) {
        this.priorigy = priorigy;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
