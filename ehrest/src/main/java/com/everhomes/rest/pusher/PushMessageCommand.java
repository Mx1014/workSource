package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

public class PushMessageCommand {
    private String deviceId;
    private String message;
    
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
