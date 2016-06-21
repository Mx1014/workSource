// @formatter:off

package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>sceneToken: 场景标识</li>
 * <li>newsToken: 新闻标识</li>
 * <li>id: 评论id</li>
 * </ul>
 */
public class DeleteNewsCommentBySceneCommand {
	@NotNull
	private String sceneToken;
	@NotNull
	private String newsToken;
	@NotNull
	private Long id;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}