// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>category: 请假类型，参考{@link com.everhomes.rest.approval.AskForLeaveCategoryDTO}</li>
 * </ul>
 */
public class CreateAskForLeaveCategoryResponse {
	private AskForLeaveCategoryDTO category;

	public CreateAskForLeaveCategoryResponse(AskForLeaveCategoryDTO category){
		this.category = category;
	}
	
	public AskForLeaveCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(AskForLeaveCategoryDTO category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
