// @formatter:off
package com.everhomes.rest.messaging;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.math.NumberUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li><p>appId:应用ID</p></li>
 * <li><p>senderUid:发送者UiD</p></li>
 * <li><p>contextType:上下文件类型</p></li>
 * <li>contextToken:上下文件token</li>
 * <li><p>channels:通道列表。参考{@link com.everhomes.rest.messaging.MessageChannel}</p></li>
 * <li><p>meta:额外要添加的信息</p></li>
 * <li><p>bodyType:消息模块的发送相关的类型 {@link }</p></li>
 * <li><p>body:消息内容</p></li>
 * <li><p>senderTag:发送者标签</p></li>
 * <li><p>storeSequence:消息体的位置游标</p></li>
 * <li><p>createTime:记录创建时间</p></li>
 * </ul>
 *
 */
public class MessageDTO implements Cloneable {
    private Long appId;
    private Long senderUid;
    
    private String contextType;
    private String contextToken;
    
    private static final String META_APP_ID = "metaAppId"; 
    
    @ItemType(MessageChannel.class)
    @NotNull
    private List<MessageChannel> channels;
    
    @ItemType(String.class)
    private Map<String, String> meta = new HashMap<String, String>();
    
    private String bodyType;

    private String body;

    // used for sender to tag on a message
    private String senderTag;
    
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
        if(this.channels == null) {
            this.channels = new ArrayList<MessageChannel>();
        }
        
        if(channelArray != null) {
            for(MessageChannel channel : channelArray)
                this.channels.add(channel);
        }
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }
    
    public String getContentDisplayText() {
        MessageBodyType type = MessageBodyType.fromCode(this.bodyType);
        if(type != null) {
            switch(type) {
            case TEXT:
                break;
                
            case IMAGE:
                return "[Image]";
                
            case AUDIO: 
                return "[Audio]";
                
            case VIDEO:
                return "[Video]";
                
            case LINK: 
                return "[Link]";
                
            case NOTIFY:  
                return "[Notify]";
                
            case RICH_LINK:
                return "[RichLink]";
            }
        }
        
        return this.body;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        String appId = null;
        Map<String, String> newMeta = meta;
        if(null == meta) {
            newMeta = new HashMap<String, String>();
        }
        
        if(null == newMeta.get(META_APP_ID) && this.meta != null && null != (appId = this.meta.get(META_APP_ID))) {
              newMeta.put(META_APP_ID, appId);
          }
        this.meta = newMeta;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setMetaAppId(Long metaAppId) {
        if(null == this.meta) {
            this.meta = new HashMap<String, String>();
        }
        this.meta.put(META_APP_ID, metaAppId.toString());
    }
    
    public Long getMetaAppId() {
      if(null == this.meta) {
            return 0l;
        }
      String appId = this.meta.get(META_APP_ID);
      if(appId == null) {
            return 0l;
        }
      return NumberUtils.toLong(appId, 0l);
    }

    public String toJson() {
        return StringHelper.toJsonString(this);
    }
    
    public static MessageDTO fromJson(String json) {
        return (MessageDTO)StringHelper.fromJsonString(json, MessageDTO.class);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
