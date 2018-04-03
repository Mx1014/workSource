// @formatter:off
package com.everhomes.rest.preview;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *  <li>id: 预览内容id</li>
 *  <li>content: 预览内容</li>
 *  <li>contentType: 预览内容类型</li>
 * </ul>
 */
public class PreviewDTO {
    private Long id;
    private String content;
    private String contentType;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
