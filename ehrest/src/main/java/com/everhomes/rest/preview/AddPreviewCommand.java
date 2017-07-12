package com.everhomes.rest.preview;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>content: 预览内容</li>
 *  <li>contentType: 预览内容类型</li>
 * </ul>
 */
public class AddPreviewCommand {

    private String content;
    private String contentType;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
