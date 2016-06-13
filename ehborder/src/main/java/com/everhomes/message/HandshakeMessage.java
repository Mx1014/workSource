package com.everhomes.message;

import java.util.Map;

public class HandshakeMessage {
//    private Integer namespaceId;
    private String deviceId;
    private String deviceType;
    private Map<String,String> meta;
    
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
    
}
