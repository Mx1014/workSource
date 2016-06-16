package com.everhomes.rest.news;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * APP端查询新闻列表
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>newsList: 新闻列表，参数{@link com.everhomes.rest.news.ListNewsAppDTO}</li>
 * </ul>
 */
public class ListNewsAppResponse {
	private Long nextPageAnchor;
	@ItemType(ListNewsAppDTO.class)
	private List<ListNewsAppDTO> newsList;

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

	public List<ListNewsAppDTO> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<ListNewsAppDTO> newsList) {
		this.newsList = newsList;
	}

}