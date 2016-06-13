// @formatter:off
package com.everhomes.rest.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>appId: 应用ID</li>
 * <li>deliveryOption: 消息发送类型。参考{@link com.everhomes.messaging.MessagingService}</li>
 * <li>messageJson: 实际消息实体的Json数据。参考{@link com.everhomes.rest.messaging.MessageDTO}</li>
 * </ul>
 */
public class SendMessageCommand {
    private Long appId;
    private Integer deliveryOption;
    
    private Long senderUid;
    
    private String contextType;
    private String contextToken;
    
    @ItemType(MessageChannel.class)
    @NotNull
    private List<MessageChannel> channels;
    
    @ItemType(String.class)
    private Map<String, String> meta;
    
    private String bodyType;
    
    private String body;

    // used for sender to tag on a message
    private String senderTag;
    
    public SendMessageCommand() {
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(Integer deliveryOption) {
        this.deliveryOption = deliveryOption;
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

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
