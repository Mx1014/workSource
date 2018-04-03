package com.everhomes.rest.ui.activity;

/**
 * 
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>categoryId: 入口id</li>
 * </ul>
 */
public class ListActivityCategoryCommand {
	private Integer namespaceId;
	private Long categoryId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
}
