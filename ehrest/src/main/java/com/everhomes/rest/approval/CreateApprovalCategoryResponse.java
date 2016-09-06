// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>category: 审批类别，参考{@link com.everhomes.rest.approval.ApprovalCategoryDTO}</li>
 * </ul>
 */
public class CreateApprovalCategoryResponse {
	private ApprovalCategoryDTO category;

	public CreateApprovalCategoryResponse(ApprovalCategoryDTO category){
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
