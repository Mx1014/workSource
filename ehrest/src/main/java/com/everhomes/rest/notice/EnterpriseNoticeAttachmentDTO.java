package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

import java.util.Date;

/**
 * <ul>
 * <li>id: 附件id</li>
 * <li>noticeId : 附件所属公告ID</li>
 * <li>name: 附件名全称:contentName+.+contentSuffix</li>
 * <li>size: 附件大小(单位为byte)</li>
 * <li>contentType: 附件类型,请查看{@link EnterpriseNoticeAttachmentContentType}</li>
 * <li>contentName : 附件名</li>
 * <li>contentSuffix : 附件后缀</li>
 * <li>contentUri: 内容uri</li>
 * <li>contentUrl: 内容url</li>
 * <li>iconUrl : 图标URL</li>
 * <li>createTime : 创建时间</li>
 * </ul>
 */
public class EnterpriseNoticeAttachmentDTO {
    private Long id;
    private Long noticeId;
    private String name;
    private Integer size;
    private String contentType;
    private String contentName;
    private String contentSuffix;
    private String contentUri;
    private String contentUrl;
    private String iconUrl;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentSuffix() {
        return contentSuffix;
    }

    public void setContentSuffix(String contentSuffix) {
        this.contentSuffix = contentSuffix;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
