package com.everhomes.rest.message;


import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 *
 * <ul>参数:
 * <li>id: 记录id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>dstChannelToken: 接收者的Token</li>
 * <li>dstChannelType: 接收者类型</li>
 * <li>status: 消息状态</li>
 * <li>appId: 应用id</li>
 * <li>messageSeq: 消息在队列中的位置</li>
 * <li>body: 内容主体</li>
 * <li>bodyType: 内容类型</li>
 * <li>deliveryOption: delivery设置</li>
 * <li>senderUid: 发送者id</li>
 * <li>channelsInfo: channel信息</li>
 * <li>senderTag: 标志</li>
 * <li>createTime: 创建消息记录的时间</li>
 * <li>sessionToken: 会话token</li>
 * <li>deviceId: 设备Id</li>
 * <li>indexId: 索引id</li>
 * <li>num: 关联数量</li>
 * </ul>
 */

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
    private Long num;

    private Map<String, String> meta;

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

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}
