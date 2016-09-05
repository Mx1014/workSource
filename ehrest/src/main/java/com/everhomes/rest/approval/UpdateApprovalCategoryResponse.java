// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>category: 请假类型，参考{@link com.everhomes.rest.approval.ApprovalCategoryDTO}</li>
 * </ul>
 */
public class UpdateApprovalCategoryResponse {
	private ApprovalCategoryDTO category;
	
	public UpdateApprovalCategoryResponse(ApprovalCategoryDTO category) {
		this.category = category;
	}

	public ApprovalCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(ApprovalCategoryDTO category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
