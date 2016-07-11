package com.everhomes.device;

public interface DeviceProvider {
    void registDevice(Device device);
    Device findDeviceByDeviceId(String deviceId);
}

