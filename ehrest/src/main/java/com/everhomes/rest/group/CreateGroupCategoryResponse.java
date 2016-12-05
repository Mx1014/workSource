// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>category: 分类信息</li>
 * </ul>
 */
public class CreateGroupCategoryResponse {

	private CategoryDTO category;

	public CreateGroupCategoryResponse() {

	}

	public CreateGroupCategoryResponse(CategoryDTO category) {
		super();
		this.category = category;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
