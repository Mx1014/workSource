package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>categories: 所有分类，参考{@link com.everhomes.rest.remind.RemindCategoryDTO}</li>
 * </ul>
 */
public class ListRemindCategoriesResponse {
    private List<RemindCategoryDTO> categories;

    public List<RemindCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<RemindCategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
