// @formatter:off

package com.everhomes.rest.news;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>newsList: 新闻列表，参考{@link com.everhomes.rest.news.BriefNewsDTO}</li>
 * </ul>
 */
public class SearchNewsResponse {
	private Long nextPageAnchor;
	@ItemType(BriefNewsDTO.class)
	private List<BriefNewsDTO> newsList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<BriefNewsDTO> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<BriefNewsDTO> newsList) {
		this.newsList = newsList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}