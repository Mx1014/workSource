package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查询新闻列表
 * <li>sceneToken: 场景标识</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNewsCommand {
	private String sceneToken;
	private Long pageAnchor;
	private Long pageSize;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

}