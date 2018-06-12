package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fileType: 附件类型</li>
 * <li>fileUri: 附件访问URI</li>
 * <li>fileUrl: 附件访问URL</li>
 * <li>fileName: 文件名称</li>
 * <li>fileSize: 文件大小 （单位字节）</li>
 * </ul>
 **/
public class ExtraEventAttachmentDTO {
	
	private String fileType;
	private String fileUri;
	private String fileUrl;
	private String fileName;
	private Long fileSize;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
