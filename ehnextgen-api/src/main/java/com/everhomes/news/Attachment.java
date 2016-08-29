// @formatter:off
package com.everhomes.news;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class Attachment implements Serializable {
	private static final long serialVersionUID = -221832334617095447L;
	private java.lang.Long id;
	private java.lang.Long ownerId;
	private java.lang.String attachmentName;
	private java.lang.String contentType;
	private java.lang.String contentUri;
	private java.lang.Long creatorUid;
	private java.sql.Timestamp createTime;

	public java.lang.Long getId() {
		return id;
	}

	public java.lang.String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(java.lang.String attachmentName) {
		this.attachmentName = attachmentName;
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

	public java.lang.String getContentUri() {
		return contentUri;
	}

	public void setContentUri(java.lang.String contentUri) {
		this.contentUri = contentUri;
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
