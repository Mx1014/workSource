// @formatter:off
package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>newsToken : 新闻token</li>
 * <li>currentPMId: 当前项目PM公司id</li>
 * </ul>
 *
 */
public class publishNewsCommand {
	private String newsToken;
	private Long currentPMId;
	

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

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}
}
