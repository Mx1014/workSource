package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>categoryId: 类型id</li>
 * </ul>
 */
public class DeleteServiceAllianceCategoryCommand {
	
	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
