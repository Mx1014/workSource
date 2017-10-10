package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <li>contentType: 附件类型，{@link com.everhomes.rest.forum.PostContentType}</li>
 * <li>contentUri: 附件访问URI</li>
 * <li>contentUrl: 附件访问URl</li>
 * <li>name: 附件名</li>
 * <li>fileSize: 附件大小</li>
 * 
 **/
public class ServiceAllianceAttachmentDTO {
	
	private Long id;
	
	private Long ownerId;
	
	private String contentType;
	
	private String contentUri; 
	
	private String contentUrl;
	
	private String name;
	
	private Integer fileSize;

	private String summaryDescription;

	private String commentToken;

	private Integer commentCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getSummaryDescription() {
		return summaryDescription;
	}

	public void setSummaryDescription(String summaryDescription) {
		this.summaryDescription = summaryDescription;
	}

	public String getCommentToken() {
		return commentToken;
	}

	public void setCommentToken(String commentToken) {
		this.commentToken = commentToken;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
