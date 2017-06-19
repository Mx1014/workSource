package com.everhomes.search;

import com.everhomes.rest.warehouse.SearchWarehouseStockLogsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseStockLogsResponse;
import com.everhomes.warehouse.WarehouseStockLogs;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/16.
 */
public interface WarehouseStockLogSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<WarehouseStockLogs> logs);
    void feedDoc(WarehouseStockLogs log);
    void syncFromDb();
    SearchWarehouseStockLogsResponse query(SearchWarehouseStockLogsCommand cmd);
}
