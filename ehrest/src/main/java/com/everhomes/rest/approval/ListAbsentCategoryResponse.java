// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>categoryList: 请假类型列表，参考{@link com.everhomes.rest.approval.AbsentCategoryDTO}</li>
 * </ul>
 */
public class ListAbsentCategoryResponse {
	@ItemType(AbsentCategoryDTO.class)
	private List<AbsentCategoryDTO> categoryList;

	public ListAbsentCategoryResponse(List<AbsentCategoryDTO> categoryList) {
		super();
		this.categoryList = categoryList;
	}

	public List<AbsentCategoryDTO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<AbsentCategoryDTO> categoryList) {
		this.categoryList = categoryList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
