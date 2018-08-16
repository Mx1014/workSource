package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>categoryList：请假类型的List{@link com.everhomes.rest.approval.ApprovalCategoryDTO}</li>
 * <li>url：假期余额跳转链接</li>
 * </ul>
 */
public class ListApprovalCategoriesResponse {
	@ItemType(ApprovalCategoryDTO.class)
    List<ApprovalCategoryDTO> categoryList;
    private String url;
    public ListApprovalCategoriesResponse() {

    }

    public ListApprovalCategoriesResponse(List<ApprovalCategoryDTO> categoryList, String url) {
        this.categoryList = categoryList;
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ApprovalCategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ApprovalCategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
