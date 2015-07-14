package com.everhomes.activity;

import java.util.List;

import com.everhomes.category.CategoryDTO;
import com.everhomes.discover.ItemType;
/**
 * 查询活动category
 * @author elians
 *activityCategories:{@link CategoryDTO}
 */
public class ListActivityCategories {
    
    @ItemType(CategoryDTO.class)
    private List<CategoryDTO> activityCategories;

    public List<CategoryDTO> getActivityCategories() {
        return activityCategories;
    }

    public void setActivityCategories(List<CategoryDTO> activityCategories) {
        this.activityCategories = activityCategories;
    }
    
}
