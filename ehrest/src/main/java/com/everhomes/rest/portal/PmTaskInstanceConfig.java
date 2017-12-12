// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>taskCategoryId: 物业报修分类id</li>
 * <li>type: 样式</li>
 * </ul>
 */
public class PmTaskInstanceConfig {

	private Long taskCategoryId;

	private String type;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
