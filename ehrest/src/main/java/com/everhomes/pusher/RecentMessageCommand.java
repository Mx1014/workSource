package com.everhomes.pusher;

/**
 * <ul>
 * <li>deviceId:设备ID</li>
 * <li>anchor:当前消息位置</li>
 * </ul>
 * 
 * @author janson
 *
 */
public class RecentMessageCommand {
    private String deviceId;
    private Long anchor;
    
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public Long getAnchor() {
        return anchor;
    }
    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }
    
    
}
