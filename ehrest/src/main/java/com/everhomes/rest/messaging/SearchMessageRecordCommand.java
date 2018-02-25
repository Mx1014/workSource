package com.everhomes.rest.messaging;

/**
 *
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>bodyType: 内容类型</li>
 * <li>senderUid: 发送者id</li>
 * <li>dstChannelToken: 接收者token</li>
 * <li>senderTag: 标志</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 页数</li>
 * <li>isGroupBy: 是否聚合</li>
 * <li>indexId: 索引id</li>
 * <li>keyWords: 搜索关键字</li>
 * </ul>
 */
public class  SearchMessageRecordCommand {
    private Integer namespaceId;
    private String bodyType;
    private Long senderUid;
    private String dstChannelToken;
    private String senderTag;
    private Long pageAnchor;
    private Integer pageSize;
    private Integer isGroupBy;
    private Long indexId;
    private String keyWords;



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

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
