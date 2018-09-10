package com.everhomes.rest.news.open;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>newsToken: 文章标识</li>
 * </ul>
 */
public class DeleteNewsCommand {
	
	@NotNull
	private String newsToken;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}


}
