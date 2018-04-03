package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>warehouses: 仓库列表 参考{@link com.everhomes.rest.warehouse.WarehouseDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/10.
 */
public class SearchWarehousesResponse {

    private Long nextPageAnchor;

    @ItemType(WarehouseDTO.class)
    private List<WarehouseDTO> warehouseDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WarehouseDTO> getWarehouseDTOs() {
        return warehouseDTOs;
    }

    public void setWarehouseDTOs(List<WarehouseDTO> warehouseDTOs) {
        this.warehouseDTOs = warehouseDTOs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
