package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查询新闻正文
 * <li>content: 正文</li>
 * </ul>
 */
public class QueryNewsContentResponse {
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