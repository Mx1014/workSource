// @formatter:off

package com.everhomes.rest.ui.news;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.news.BriefNewsDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 返回值
 * <li>nextPageAnchor: 分页的锚点，下一页开始取数据的位置</li>
 * <li>newsList: 新闻列表，参考{@link com.everhomes.rest.news.BriefNewsDTO}</li>
 * <li>needUseUrl: “查看更多”是否进行跳转 {@link com.everhomes.rest.news.NewsNormalFlag}  0-不启用 1-启用</li>
 * <li>renderUrl: “查看更多”跳转url</li>
 * <li>title: 跳转后的页面标题</li>
 * </ul>
 */
public class ListNewsBySceneResponse {
	private Long nextPageAnchor;
	@ItemType(BriefNewsDTO.class)
	private List<BriefNewsDTO> newsList;
	
	private Byte needUseUrl;
	
	private String renderUrl;
	
	private String title;
	

	public Byte getNeedUseUrl() {
		return needUseUrl;
	}

	public void setNeedUseUrl(Byte needUseUrl) {
		this.needUseUrl = needUseUrl;
	}

	public String getRenderUrl() {
		return renderUrl;
	}

	public void setRenderUrl(String renderUrl) {
		this.renderUrl = renderUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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