package com.everhomes.rest.user;

/**
 * <ul>
 *  <li>targetFieldName:字段展示名</li>
 *  <li>contentType:附件类型</li>
 *  <li>contentUri:附件上传后的url地址 </li>
 * </ul>
 */
public class RequestAttachmentsDTO {

	private String targetFieldName;
	
	private String contentType;
	
	private String contentUri;

	public String getTargetFieldName() {
		return targetFieldName;
	}

	public void setTargetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
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

}
