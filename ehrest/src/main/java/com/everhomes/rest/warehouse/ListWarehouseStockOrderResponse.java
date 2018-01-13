//@formatter:off
package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/11.
 */
/**
 *<ul>
 * <li>nextPageAnchor:下一个锚点</li>
 * <li>dtos:出入库列表数据，参考{@link com.everhomes.rest.warehouse.WarehouseStockOrderDTO}</li>
 *</ul>
 */
public class ListWarehouseStockOrderResponse {
    private Long nextPageAnchor;
    private List<WarehouseStockOrderDTO> dtos;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WarehouseStockOrderDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<WarehouseStockOrderDTO> dtos) {
        this.dtos = dtos;
    }
}
