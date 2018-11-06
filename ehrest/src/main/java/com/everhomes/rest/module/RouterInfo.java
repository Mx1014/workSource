package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>path: 跳转路径</li>
 *     <li>query: 跳转参数，例如：论坛的query为forumId=xxx&topicId=yyy</li>
 * </ul>
 */
public class RouterInfo {

	private Long moduleId;
	private String path;

	private String query;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
