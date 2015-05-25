// @formatter:off
package com.everhomes.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>appId: 应用ID</li>
 * <li>senderUid: 发送者UiD</li>
 * <li>channels: 通道列表。参考{@link com.everhomes.messaging.MessageChannel}</li>
 * <li>meta: 额外的消息信息</li>
 * <li>metaAppId: 消息模块的发送相关的应用ID</li>
 * <li>body: 消息内容</li>
 * <li>senderTag: 发送者标签</li>
 * <li>storeSequence: 消息体的位置游标</li>
 * </ul>
 *
 */
public class MessageDTO {
    private Long appId;
    private Long senderUid;
    
    private String contextType;
    private String contextToken;
    
    @ItemType(MessageChannel.class)
    @NotNull
    private List<MessageChannel> channels;
    
    @ItemType(String.class)
    private Map<String, String> meta;
    
    private Long metaAppId;
    
    private String body;

    // used for sender to tag on a message
    private String senderTag;
    
    // unique message sequence number
    private Long messageSequence;
    
    // used to indicate the message store(anchor) position in message responses
    private Long storeSequence;
    
    private Long createTime;
     
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

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getContextToken() {
        return contextToken;
    }

    public void setContextToken(String contextToken) {
        this.contextToken = contextToken;
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

    public Long getMetaAppId() {
        return metaAppId;
    }

    public void setMetaAppId(Long metaAppId) {
        this.metaAppId = metaAppId;
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

    public Long getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(Long messageSequence) {
        this.messageSequence = messageSequence;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String toJson() {
        return StringHelper.toJsonString(this);
    }
    
    public static MessageDTO fromJson(String json) {
        return (MessageDTO)StringHelper.fromJsonString(json, MessageDTO.class);
    }
}
