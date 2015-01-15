package com.everhomes.user;

public class MessageChannel {
    private String channelType;
    private String channelToken;
    
    public MessageChannel() {
    }
    
    public MessageChannel(String channelType, String channelToken) {
        this.channelType = channelType;
        this.channelToken = channelToken;
    }
    
    public String getChannelType() {
        return channelType;
    }
    
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
    
    public String getChannelToken() {
        return channelToken;
    }
    
    public void setChannelToken(String channelToken) {
        this.channelToken = channelToken;
    }
}
