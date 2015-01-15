package com.everhomes.rpc.client;

import java.util.HashMap;
import java.util.Map;

import com.everhomes.util.Name;

@Name("msg.realtime")
public class RealtimeMessageIndicationPdu {
    private long senderUid;
    private Long appId;
    private String channelType;
    private String channelToken;
    private String content;
    private String senderTag;
    private long createTime;
    
    private Map<String, String> metaMap = new HashMap<String, String>();

    public RealtimeMessageIndicationPdu() {
    }

    public long getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(long senderUid) {
        this.senderUid = senderUid;
    }

    public Long getAppId() {
        return this.appId;
    }
    
    public void setAppId(Long appId) {
        this.appId = appId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderTag() {
        return senderTag;
    }

    public void setSenderTag(String senderTag) {
        this.senderTag = senderTag;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Map<String, String> getMetaMap() {
        return metaMap;
    }

    public void setMetaMap(Map<String, String> metaMap) {
        this.metaMap = metaMap;
    }
}
