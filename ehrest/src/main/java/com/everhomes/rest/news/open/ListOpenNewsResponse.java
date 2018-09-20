package com.everhomes.rest.news.open;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>nextPageAnchor: 下一页的锚点,如还有下一页返回。否则为空</li>
 * <li>newsList: 文章列表 {@link com.everhomes.rest.news.open.OpenBriefNewsDTO}</li>
 * </ul>
 */
public class ListOpenNewsResponse {
	private Long nextPageAnchor;
	private List<OpenBriefNewsDTO> newsList;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<OpenBriefNewsDTO> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<OpenBriefNewsDTO> newsList) {
		this.newsList = newsList;
	}
}
