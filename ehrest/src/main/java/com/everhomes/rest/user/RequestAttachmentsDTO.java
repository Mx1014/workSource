package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>targetFieldName:字段展示名</li>
 *  <li>contentType:附件类型</li>
 *  <li>contentUri:附件上传后的url地址 </li>
 *  <li>fileName:附件的名称（带格式的名称） </li>
 * </ul>
 */
public class RequestAttachmentsDTO {

	private String targetFieldName;
	
	private String contentType;
	
	private String contentUri;

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
