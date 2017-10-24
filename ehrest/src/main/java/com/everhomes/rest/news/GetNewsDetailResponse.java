package com.everhomes.rest.news;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * 返回值
 * <li>title: 标题</li>
 * <li>publishTime: 发布时间</li>
 * <li>author: 作者</li>
 * <li>sourceDesc: 来源</li>
 * <li>sourceUrl: 原文链接</li>
 * <li>content: 正文</li>
 * <li>contentAbstract: 摘要</li>
 * <li>phone: 联系方式</li>
 * <li>newsUrl: 新闻链接-供分享</li>
 * <li>communityIds: 可见范围</li>
 * <li>coverUri: 封面</li>
 * <li>newsTags: 标签值</li>
 * </ul>
 */
public class GetNewsDetailResponse {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private Long categoryId;
    private String title;
    private String contentAbstract;
    private String coverUri;
    private String content;
    private String author;
    private Long publishTime;
    private String sourceDesc;
    private String sourceUrl;
    private Long phone;
    @ItemType(Long.class)
    private List<String> communityIds;
    @ItemType(NewsTagDTO.class)
    private List<NewsTagDTO> newsTags;
    private String visibleType;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentAbstract() {
        return contentAbstract;
    }

    public void setContentAbstract(String contentAbstract) {
        this.contentAbstract = contentAbstract;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public List<String> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<String> communityIds) {
        this.communityIds = communityIds;
    }

    public List<NewsTagDTO> getNewsTags() {
        return newsTags;
    }

    public void setNewsTags(List<NewsTagDTO> newsTags) {
        this.newsTags = newsTags;
    }

    public String getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(String visibleType) {
        this.visibleType = visibleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
