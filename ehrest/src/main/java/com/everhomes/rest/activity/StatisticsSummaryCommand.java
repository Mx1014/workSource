// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>categoryId: 活动类型id</li>
 *     <li>contentCategoryId: 内容类型ID，{@link com.everhomes.rest.category.CategoryConstants}</li>
 * </ul>
 */
public class StatisticsSummaryCommand {
	private Integer namespaceId;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
