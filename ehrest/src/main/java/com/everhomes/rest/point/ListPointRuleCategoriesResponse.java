package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>modules: modules {@link PointRuleCategoryDTO}</li>
 * </ul>
 */
public class ListPointRuleCategoriesResponse {

    @ItemType(PointRuleCategoryDTO.class)
    private List<PointRuleCategoryDTO> categories;

    public List<PointRuleCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<PointRuleCategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
