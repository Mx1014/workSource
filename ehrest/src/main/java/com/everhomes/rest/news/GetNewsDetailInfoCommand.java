// @formatter:off

package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>newsToken: 新闻标识</li>
 * </ul>
 */
public class GetNewsDetailInfoCommand {
	@NotNull
	private String newsToken;

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}