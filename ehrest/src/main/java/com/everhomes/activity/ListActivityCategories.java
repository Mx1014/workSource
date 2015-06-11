package com.everhomes.activity;

import java.util.List;

import com.everhomes.category.CategoryDTO;
/**
 * 查询活动category
 * @author elians
 *activityCategories:{@link CategoryDTO}
 */
public class ListActivityCategories {
    private List<CategoryDTO> activityCategories;

    public List<CategoryDTO> getActivityCategories() {
        return activityCategories;
    }

    public void setActivityCategories(List<CategoryDTO> activityCategories) {
        this.activityCategories = activityCategories;
    }
    
}
