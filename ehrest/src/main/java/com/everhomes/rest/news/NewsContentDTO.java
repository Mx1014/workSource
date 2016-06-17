package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>content: 正文</li>
 * </ul>
 */
public class NewsContentDTO {
	private String content;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}