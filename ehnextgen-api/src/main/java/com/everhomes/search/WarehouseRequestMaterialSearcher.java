package com.everhomes.search;

import com.everhomes.rest.warehouse.QueryRequestCommand;
import com.everhomes.rest.warehouse.SearchRequestsResponse;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsCommand;
import com.everhomes.warehouse.WarehouseRequestMaterials;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/17.
 */
public interface WarehouseRequestMaterialSearcher {

    void deleteById(Long id);
    void bulkUpdate(List<WarehouseRequestMaterials> materials);
    void feedDoc(WarehouseRequestMaterials material);
    void syncFromDb();
    List<Long> query(QueryRequestCommand cmd);
}
