// @formatter:off
package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>newsToken : 新闻token</li>
 * </ul>
 *
 */
public class publishNewsCommand {
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
