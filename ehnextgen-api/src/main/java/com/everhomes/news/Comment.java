// @formatter:off
package com.everhomes.news;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class Comment implements Serializable{
	private static final long serialVersionUID = 1956250310373081291L;

	private java.lang.Long id;
	private java.lang.Long ownerId;
	private java.lang.String contentType;
	private java.lang.String content;
	private java.lang.Byte status;
	private java.lang.Long creatorUid;
	private java.sql.Timestamp createTime;
	private java.lang.Long deleterUid;
	private java.sql.Timestamp deleteTime;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(java.lang.Long ownerId) {
		this.ownerId = ownerId;
	}

	public java.lang.String getContentType() {
		return contentType;
	}

	public void setContentType(java.lang.String contentType) {
		this.contentType = contentType;
	}

	public java.lang.String getContent() {
		return content;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public java.lang.Byte getStatus() {
		return status;
	}

	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}

	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public java.lang.Long getDeleterUid() {
		return deleterUid;
	}

	public void setDeleterUid(java.lang.Long deleterUid) {
		this.deleterUid = deleterUid;
	}

	public java.sql.Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(java.sql.Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
