// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>taskCategoryId: 物业报修分类id</li>
 *     <li>type: 样式</li>
 *     <li>url: url</li>
 * </ul>
 */
public class PmTaskInstanceConfig {

	private Long taskCategoryId;

	private String type;

	private String url;

	public Long getTaskCategoryId() {
		return taskCategoryId;
	}

	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
