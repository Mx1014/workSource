package com.everhomes.search;

import com.everhomes.rest.warehouse.SearchWarehouseMaterialCategoriesCommand;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialCategoriesResponse;
import com.everhomes.warehouse.WarehouseMaterialCategories;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public interface WarehouseMaterialCategorySearcher {

    void deleteById(Long id);
    void bulkUpdate(List<WarehouseMaterialCategories> categories);
    void feedDoc(WarehouseMaterialCategories category);
    void syncFromDb();
    SearchWarehouseMaterialCategoriesResponse query(SearchWarehouseMaterialCategoriesCommand cmd);
}
