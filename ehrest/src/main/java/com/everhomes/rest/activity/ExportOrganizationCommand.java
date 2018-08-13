package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>categoryId: 活动类型id</li>
 *     <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ExportOrganizationCommand {

	private Long categoryId;
	private Integer namespaceId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
