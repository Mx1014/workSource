package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class OpPromotionMessageDTO {
    private Long     messageSeq;
    private Long     metaAppId;
    private String     resultData;
    private String     messageText;
    private String     ownerType;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     messageMeta;
    private Long     ownerId;
    private Long     senderUid;
    private Long     targetUid;
    private Long     id;

    

    public Long getMessageSeq() {
        return messageSeq;
    }



    public void setMessageSeq(Long messageSeq) {
        this.messageSeq = messageSeq;
    }



    public Long getMetaAppId() {
        return metaAppId;
    }



    public void setMetaAppId(Long metaAppId) {
        this.metaAppId = metaAppId;
    }



    public String getResultData() {
        return resultData;
    }



    public void setResultData(String resultData) {
        this.resultData = resultData;
    }



    public String getMessageText() {
        return messageText;
    }



    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }



    public String getOwnerType() {
        return ownerType;
    }



    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }



    public Timestamp getCreateTime() {
        return createTime;
    }



    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public Integer getNamespaceId() {
        return namespaceId;
    }



    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }



    public String getMessageMeta() {
        return messageMeta;
    }



    public void setMessageMeta(String messageMeta) {
        this.messageMeta = messageMeta;
    }



    public Long getOwnerId() {
        return ownerId;
    }



    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }



    public Long getSenderUid() {
        return senderUid;
    }



    public void setSenderUid(Long senderUid) {
        this.senderUid = senderUid;
    }



    public Long getTargetUid() {
        return targetUid;
    }



    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
