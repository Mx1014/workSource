package com.everhomes.rest.rpc.server;

import com.everhomes.util.Name;
import com.everhomes.util.StringHelper;

@Name("client.pusherNotify")
public class PusherNotifyPdu {
    private long messageId;
    private String messageType;
    private String deviceId;
    private String platform;
    private String audience;
    private String notification;
    
//    public enum MessageType {
//        UNICAST("UNICAST"),
//        MULTICAST("MULTICAST");
//        private final String text;
//        
//        private MessageType(final String text){
//            this.text = text;
//        }
//        
//        @Override
//        public String toString() {
//            return text;
//        }
//    }
    
    public long getMessageId() {
        return messageId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getAudience() {
        return audience;
    }
    public void setAudience(String audience) {
        this.audience = audience;
    }
    public String getNotification() {
        return notification;
    }
    public void setNotification(String notification) {
        this.notification = notification;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
