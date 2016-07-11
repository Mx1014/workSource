package com.everhomes.aclink.lingling;

import com.everhomes.util.StringHelper;

public class AclinkLinglingDevice {
    private Long deviceId;
    private String newDeviceName;
    private String newDeviceCode;
    private String deviceName;
    private String deviceCode;
    
    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public String getNewDeviceName() {
        return newDeviceName;
    }
    public void setNewDeviceName(String newDeviceName) {
        this.newDeviceName = newDeviceName;
    }
    public String getNewDeviceCode() {
        return newDeviceCode;
    }
    public void setNewDeviceCode(String newDeviceCode) {
        this.newDeviceCode = newDeviceCode;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceCode() {
        return deviceCode;
    }
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
