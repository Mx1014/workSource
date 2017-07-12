// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 *   <li>categoryId: 活动类型id</li>
 *   <li>contentCategoryId: 内容类型ID，{@link com.everhomes.rest.category.CategoryConstants}</li>
 * </ul>
 */
public class StatisticsSummaryCommand {
	private Long categoryId;
	private Long contentCategoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getContentCategoryId() {
		return contentCategoryId;
	}

	public void setContentCategoryId(Long contentCategoryId) {
		this.contentCategoryId = contentCategoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
