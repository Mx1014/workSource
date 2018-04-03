// @formatter:off
package com.everhomes.rest.link;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 链接ID</li>
 * <li>ownerUid: 链接owner id</li>
 * <li>sourceType: 来源类型，{@link com.everhomes.rest.link.LinkSourceType}</li>
 * <li>sourceId: 来源ID</li>
 * <li>title: 标题</li>
 * <li>author: 作者</li>
 * <li>coverUri: 封面URL</li>
 * <li>contentType: 内容类型，{@link com.everhomes.rest.link.LinkContentType}</li>
 * <li>content: 内容</li>
 * <li>contentAbstract: 内容摘要</li>
 * </ul>
 */
public class LinkDTO{

	public LinkDTO() {
	}
	
	private Long     id;
	private Long     ownerUid;
	private Byte     sourceType;
	private Long     sourceId;
	private String   title;
	private String   author;
	private String   coverUri;
	private String   contentType;
	private String   content;
	private String   contentAbstract;
	private Byte     status;
	private Timestamp createTime;
	private Long     deleterUid;
	private Timestamp deleteTime;
	private String   richContent;
	
	 public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public Byte getSourceType() {
        return sourceType;
    }

    public void setSourceType(Byte sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentAbstract() {
        return contentAbstract;
    }

    public void setContentAbstract(String contentAbstract) {
        this.contentAbstract = contentAbstract;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getDeleterUid() {
        return deleterUid;
    }

    public void setDeleterUid(Long deleterUid) {
        this.deleterUid = deleterUid;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getRichContent() {
		return richContent;
	}

	public void setRichContent(String richContent) {
		this.richContent = richContent;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
