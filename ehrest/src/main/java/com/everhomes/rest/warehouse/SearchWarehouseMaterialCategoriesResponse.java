package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>categoryDTOs: 物品分类列表 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialCategoryDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchWarehouseMaterialCategoriesResponse {
    private Long nextPageAnchor;

    @ItemType(WarehouseMaterialCategoryDTO.class)
    private List<WarehouseMaterialCategoryDTO> categoryDTOs;

    public List<WarehouseMaterialCategoryDTO> getCategoryDTOs() {
        return categoryDTOs;
    }

    public void setCategoryDTOs(List<WarehouseMaterialCategoryDTO> categoryDTOs) {
        this.categoryDTOs = categoryDTOs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
