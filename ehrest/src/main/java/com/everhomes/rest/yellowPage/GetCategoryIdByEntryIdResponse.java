// @formatter:off
package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>categoryId : 服务联盟应用入口ID</li>
 * </ul>
 *
 *  @author:dengs 2017年9月21日
 */
public class GetCategoryIdByEntryIdResponse {
	private Long categoryId;

	public GetCategoryIdByEntryIdResponse() {
		super();
	}

	public GetCategoryIdByEntryIdResponse(Long categoryId) {
		super();
		this.categoryId = categoryId;
	}

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
