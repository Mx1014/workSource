package com.everhomes.rest.aclink;

public class DoorAccessDeviceDTO {

    private String devUnique;
    private String deviceName;
    private Byte deviceType;

    public String getDevUnique() {
        return devUnique;
    }

    public void setDevUnique(String devUnique) {
        this.devUnique = devUnique;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Byte deviceType) {
        this.deviceType = deviceType;
    }
}
