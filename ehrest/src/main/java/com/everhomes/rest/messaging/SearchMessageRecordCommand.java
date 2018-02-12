package com.everhomes.rest.messaging;

public class  SearchMessageRecordCommand {
    private Integer namespaceId;
    private String bodyType;
    private Long senderUid;
    private String dstChannelToken;
    private String senderTag;
    private Long pageAnchor;
    private Integer pageSize;
    private Integer isGroupBy;



    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public Long getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(Long senderUid) {
        this.senderUid = senderUid;
    }

    public String getDstChannelToken() {
        return dstChannelToken;
    }

    public void setDstChannelToken(String dstChannelToken) {
        this.dstChannelToken = dstChannelToken;
    }

    public String getSenderTag() {
        return senderTag;
    }

    public void setSenderTag(String senderTag) {
        this.senderTag = senderTag;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getIsGroupBy() {
        return isGroupBy;
    }

    public void setIsGroupBy(Integer isGroupBy) {
        this.isGroupBy = isGroupBy;
    }
}
