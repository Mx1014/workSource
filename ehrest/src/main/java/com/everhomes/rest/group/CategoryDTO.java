// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>namespaceId: 域空间id</li>
 * <li>categoryId: 分类id</li>
 * <li>categoryName: 分类名称</li>
 * </ul>
 */
public class CategoryDTO {

	private Integer namespaceId;

	private Long categoryId;

	private String categoryName;

	public CategoryDTO() {

	}

	public CategoryDTO(Integer namespaceId, Long categoryId, String categoryName) {
		super();
		this.namespaceId = namespaceId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
