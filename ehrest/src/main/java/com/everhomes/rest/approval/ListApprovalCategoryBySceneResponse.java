// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>categoryList: 请假类型列表，参考{@link com.everhomes.rest.approval.ApprovalCategoryDTO}</li>
 * </ul>
 */
public class ListApprovalCategoryBySceneResponse {
	@ItemType(ApprovalCategoryDTO.class)
	private List<ApprovalCategoryDTO> categoryList;

	public ListApprovalCategoryBySceneResponse(List<ApprovalCategoryDTO> categoryList) {
		super();
		this.categoryList = categoryList;
	}

	public List<ApprovalCategoryDTO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<ApprovalCategoryDTO> categoryList) {
		this.categoryList = categoryList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
