package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> mediaId: 文件的id</li>
 *  <li> fileName: 文件名（带后缀）</li>
 * </ul>
 */
public class GetContentServerUrlCommand {

	private String mediaId;
	
	private String fileName;
	
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
