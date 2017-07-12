package com.everhomes.search;

import com.everhomes.rest.warehouse.SearchWarehousesCommand;
import com.everhomes.rest.warehouse.SearchWarehousesResponse;
import com.everhomes.warehouse.Warehouses;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public interface WarehouseSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Warehouses> warehouses);
    void feedDoc(Warehouses warehouse);
    void syncFromDb();
    SearchWarehousesResponse query(SearchWarehousesCommand cmd);
}
