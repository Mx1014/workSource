package com.everhomes.controller;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class DeviceInfo {
    private String deviceId;
    
    private String deviceType;
    private Map<String,String> meta;
    
    AtomicBoolean valid = new AtomicBoolean(false);
    AtomicLong lastPingTime = new AtomicLong(0);
    
    public DeviceInfo() {
        
    }
    public DeviceInfo(long now) {
        lastPingTime.set(now);
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public long getLastPingTime() {
        return lastPingTime.get();
    }
    public void setLastPingTime(long lastPingTime) {
        this.lastPingTime.set(lastPingTime);
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public Map<String, String> getMeta() {
        return meta;
    }
    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
    
    public boolean isValid() {
        return valid.get();
    }
    
    public void setValid(boolean b) {
        valid.set(b);
    }
}
