package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查询新闻详情
 * <li>sceneToken: 场景标识</li>
 * <li>id: 新闻id</li>
 * </ul>
 */
public class QueryNewsDetailCommand {
	private String sceneToken;
	@NotNull
	private Long id;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}