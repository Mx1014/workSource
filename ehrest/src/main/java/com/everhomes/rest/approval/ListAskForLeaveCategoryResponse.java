// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>categoryList: 请假类型列表，参考{@link com.everhomes.rest.approval.AskForLeaveCategoryDTO}</li>
 * </ul>
 */
public class ListAskForLeaveCategoryResponse {
	@ItemType(AskForLeaveCategoryDTO.class)
	private List<AskForLeaveCategoryDTO> categoryList;

	public ListAskForLeaveCategoryResponse(List<AskForLeaveCategoryDTO> categoryList) {
		super();
		this.categoryList = categoryList;
	}

	public List<AskForLeaveCategoryDTO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<AskForLeaveCategoryDTO> categoryList) {
		this.categoryList = categoryList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
