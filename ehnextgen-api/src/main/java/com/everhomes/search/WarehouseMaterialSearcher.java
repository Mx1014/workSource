package com.everhomes.search;

import com.everhomes.rest.warehouse.SearchWarehouseMaterialsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsResponse;
import com.everhomes.warehouse.WarehouseMaterials;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public interface WarehouseMaterialSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<WarehouseMaterials> materials);
    void feedDoc(WarehouseMaterials material);
    void syncFromDb();
    SearchWarehouseMaterialsResponse query(SearchWarehouseMaterialsCommand cmd);
}
