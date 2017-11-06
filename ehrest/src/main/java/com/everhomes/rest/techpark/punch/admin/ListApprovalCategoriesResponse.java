package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>categoryList：请假类型的List</li>
 * </ul>
 */
public class ListApprovalCategoriesResponse {
    public ListApprovalCategoriesResponse() {

    }

    public ListApprovalCategoriesResponse(List<ApprovalCategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }
    @ItemType(ApprovalCategoryDTO.class)
    List<ApprovalCategoryDTO> categoryList;

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

}
