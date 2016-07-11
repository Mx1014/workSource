package com.everhomes.messaging;

public class NotifyMessage {
    private String deviceId;
   
    //TODO for meta information like media/audio
    //private Map<String,String> meta;
    
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
    
}
