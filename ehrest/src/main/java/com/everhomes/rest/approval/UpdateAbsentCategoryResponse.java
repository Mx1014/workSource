// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>category: 请假类型，参考{@link com.everhomes.rest.approval.AbsentCategoryDTO}</li>
 * </ul>
 */
public class UpdateAbsentCategoryResponse {
	private AbsentCategoryDTO category;
	
	public UpdateAbsentCategoryResponse(AbsentCategoryDTO category) {
		this.category = category;
	}

	public AbsentCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(AbsentCategoryDTO category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
