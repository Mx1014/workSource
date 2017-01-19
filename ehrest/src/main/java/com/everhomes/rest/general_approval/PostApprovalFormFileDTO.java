package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>uri: 文件的链接</li>
 * <li>fileName: 文件名</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormFileDTO {

	private String uri;
	private String fileName;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
