package com.everhomes.rest.news;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 后台查询新闻列表
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>newsList: 新闻列表，参数{@link com.everhomes.rest.news.ListNewsAdminDTO}</li>
 * </ul>
 */
public class ListNewsAdminResponse {
	private Long nextPageAnchor;
	@ItemType(ListNewsAdminDTO.class)
	private List<ListNewsAdminDTO> newsList;

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

	public List<ListNewsAdminDTO> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<ListNewsAdminDTO> newsList) {
		this.newsList = newsList;
	}

}