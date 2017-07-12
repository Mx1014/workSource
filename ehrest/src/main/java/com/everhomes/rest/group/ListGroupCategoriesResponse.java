// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>categories: 分类列表</li>
 * </ul>
 */
public class ListGroupCategoriesResponse {

	@ItemType(CategoryDTO.class)
	private List<CategoryDTO> categories;

	public ListGroupCategoriesResponse() {

	}

	public ListGroupCategoriesResponse(List<CategoryDTO> categories) {
		super();
		this.categories = categories;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
