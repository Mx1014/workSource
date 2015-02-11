// @formatter:off
package com.everhomes.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class MessageData {
    private Long appId;
    private List<MessageChannel> channels;
    private Map<String, String> meta;
    private String body;

    private static Gson gson = new Gson();
    
    public MessageData() {
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<MessageChannel> channels) {
        this.channels = channels;
    }
    
    public void setChannels(MessageChannel... channelArray) {
        if(channelArray != null) {
            this.channels = new ArrayList<MessageChannel>();
            
            for(MessageChannel channel : channelArray)
                this.channels.add(channel);
        }
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public String toJson() {
        return gson.toJson(this);
    }
    
    public static MessageData fromJson(String json) {
        return gson.fromJson(json, MessageData.class);
    }
}