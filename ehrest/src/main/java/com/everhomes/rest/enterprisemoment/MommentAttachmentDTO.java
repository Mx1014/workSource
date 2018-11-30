package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contentName: 附件名</li>
 * <li>contentSuffix: 附件后缀名</li>
 * <li>size: 附件大小</li>
 * <li>contentType: 附件类型</li>
 * <li>contentUri: 附件uri</li>
 * <li>contentUrl: 附件url</li>
 * </ul>
 */

public class MommentAttachmentDTO {
    private String contentName;
    private String contentSuffix;
    private Integer size;
    private String contentType;
    private String contentUri;
    private String contentUrl;


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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
    
}
