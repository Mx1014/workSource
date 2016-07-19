package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

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
    private Integer namespaceId;
    private String deviceId;
    private Long anchor;
    private Integer count;
    
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
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }
    
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
