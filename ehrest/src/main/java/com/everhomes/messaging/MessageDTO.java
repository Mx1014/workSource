// @formatter:off
package com.everhomes.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;

/**
 * <ul>
 * <li>appId: 应用ID</li>
 * <li>senderUid: 发送者UiD</li>
 * <li>channels: 通道列表。参考{@link com.everhomes.messaging.MessageChannel}</li>
 * <li>meta: 额外的消息信息</li>
 * <li>body: 消息内容</li>
 * <li>senderTag: 发送者标签</li>
 * <li>storeSequence: 消息体的位置游标</li>
 * </ul>
 *
 */
public class MessageDTO {
    private Long appId;
    private Long senderUid;
    
    @NotNull
    private List<MessageChannel> channels;
    private Map<String, String> meta;
    
    private String body;

    // used for sender to tag on a message
    private String senderTag;
    
    // used to indicate the message store(anchor) position in message responses
    private Long storeSequence;

    private static Gson gson = new Gson();
    
    public MessageDTO() {
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
    
    public Long getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(Long senderUid) {
        this.senderUid = senderUid;
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
    
    public String getSenderTag() {
        return senderTag;
    }

    public void setSenderTag(String senderTag) {
        this.senderTag = senderTag;
    }

    public Long getStoreSequence() {
        return storeSequence;
    }

    public void setStoreSequence(Long storeSequence) {
        this.storeSequence = storeSequence;
    }

    public String toJson() {
        return gson.toJson(this);
    }
    
    public static MessageDTO fromJson(String json) {
        return gson.fromJson(json, MessageDTO.class);
    }
}
