package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>stockDTOs: 库存日志列表 参考{@link com.everhomes.rest.warehouse.WarehouseStockLogDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchWarehouseStockLogsResponse {

    private Long nextPageAnchor;

    @ItemType(WarehouseStockLogDTO.class)
    private List<WarehouseStockLogDTO> stockLogDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WarehouseStockLogDTO> getStockLogDTOs() {
        return stockLogDTOs;
    }

    public void setStockLogDTOs(List<WarehouseStockLogDTO> stockLogDTOs) {
        this.stockLogDTOs = stockLogDTOs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
