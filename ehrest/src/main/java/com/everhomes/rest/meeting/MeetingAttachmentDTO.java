package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id : id创建时不用传</li>
 * <li>contentName :  附件名称</li>
 * <li>contentType :  附件类型 后缀名</li>
 * <li>contentUri : 附件uri contentserver的uri</li>
 * <li>contentUrl : 附件url 直接下载访问的链接</li>
 * <li>contentSize :  附件大小</li>
 * <li>contentIconUri : 附件图标uri</li>
 * <li>contentIconUrl : 附件图标url</li>
 * </ul>
 */
public class MeetingAttachmentDTO {
	private Long id;
    private String contentName;
    private String contentType;
    private String contentUri;
    private String contentUrl;
    private Integer contentSize;
    private String contentIconUri;
    private String contentIconUrl;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
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

	public Integer getContentSize() {
		return contentSize;
	}

	public void setContentSize(Integer contentSize) {
		this.contentSize = contentSize;
	}

	public String getContentIconUri() {
		return contentIconUri;
	}

	public void setContentIconUri(String contentIconUri) {
		this.contentIconUri = contentIconUri;
	}

	public String getContentIconUrl() {
		return contentIconUrl;
	}

	public void setContentIconUrl(String contentIconUrl) {
		this.contentIconUrl = contentIconUrl;
	}
    
    
}
