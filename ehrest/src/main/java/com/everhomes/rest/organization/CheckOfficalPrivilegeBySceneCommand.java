// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>sceneToken : 场景标识</li>
 * 	<li>categoryId : 类型id</li>
 * </ul>
 *
 */
public class CheckOfficalPrivilegeBySceneCommand {
	private String sceneToken;

	private Long categoryId;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
