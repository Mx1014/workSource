package com.everhomes.rest.module;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 路由类型，例如：应用主页、内容详情等，需要和使用者约定</li>
 *     <li>path: 跳转路径</li>
 *     <li>query: 跳转参数，例如：论坛的query为forumId=xxx&topicId=yyy</li>
 * </ul>
 */
public class RouterInfo {

	String name;

	String path;

	String query;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
