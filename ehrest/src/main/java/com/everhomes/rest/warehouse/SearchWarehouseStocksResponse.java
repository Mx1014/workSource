package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>stockDTOs: 库存物品列表 参考{@link com.everhomes.rest.warehouse.WarehouseStockDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchWarehouseStocksResponse {

    private Long nextPageAnchor;

    @ItemType(WarehouseStockDTO.class)
    private List<WarehouseStockDTO> stockDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WarehouseStockDTO> getStockDTOs() {
        return stockDTOs;
    }

    public void setStockDTOs(List<WarehouseStockDTO> stockDTOs) {
        this.stockDTOs = stockDTOs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
