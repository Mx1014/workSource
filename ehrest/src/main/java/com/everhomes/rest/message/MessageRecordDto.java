package com.everhomes.rest.message;


import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.sql.Timestamp;

public class MessageRecordDto implements Serializable {
    private Long id;
    private Integer namespaceId;
    private String dstChannelToken;
    private String dstChannelType;
    private String status;
    private Long appId;
    private Long messageSeq;
    private Long senderUid;
    private String senderTag;
    private String channelsInfo;
    private String bodyType;
    private String body;
    private Integer deliveryOption;
    private Timestamp createTime;
    private String sessionToken;
    private String deviceId;
    private Long indexId;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return this.namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getAppId() {
        return this.appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getMessageSeq() {
        return this.messageSeq;
    }

    public void setMessageSeq(Long messageSeq) {
        this.messageSeq = messageSeq;
    }

    public Long getSenderUid() {
        return this.senderUid;
    }

    public void setSenderUid(Long senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderTag() {
        return this.senderTag;
    }

    public void setSenderTag(String senderTag) {
        this.senderTag = senderTag;
    }

    public String getDstChannelType() {
        return this.dstChannelType;
    }

    public void setDstChannelType(String dstChannelType) {
        this.dstChannelType = dstChannelType;
    }

    public String getDstChannelToken() {
        return this.dstChannelToken;
    }

    public void setDstChannelToken(String dstChannelToken) {
        this.dstChannelToken = dstChannelToken;
    }

    public String getChannelsInfo() {
        return this.channelsInfo;
    }

    public void setChannelsInfo(String channelsInfo) {
        this.channelsInfo = channelsInfo;
    }

    public String getBodyType() {
        return this.bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public Integer getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(Integer deliveryOption) {
        this.deliveryOption = deliveryOption;
    }
}
