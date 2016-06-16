package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 批量导入新闻
 * <li>sceneToken: 场景标识</li>
 * </ul>
 */
public class ImportNewsCommand {
	private String sceneToken;

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

}