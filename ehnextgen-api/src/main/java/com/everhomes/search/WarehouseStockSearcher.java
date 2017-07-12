package com.everhomes.search;

import com.everhomes.rest.warehouse.SearchWarehouseStocksCommand;
import com.everhomes.rest.warehouse.SearchWarehouseStocksResponse;
import com.everhomes.warehouse.WarehouseStocks;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/15.
 */
public interface WarehouseStockSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<WarehouseStocks> stocks);
    void feedDoc(WarehouseStocks stock);
    void syncFromDb();
    SearchWarehouseStocksResponse query(SearchWarehouseStocksCommand cmd);
}
