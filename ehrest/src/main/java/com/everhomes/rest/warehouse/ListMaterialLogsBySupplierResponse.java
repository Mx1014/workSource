//@formatter:off
package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>nextPageAnchor:下一个锚点</li>
 * <li>dtos:供货记录数据列表，参考{@link com.everhomes.rest.warehouse.WarehouseLogDTO}</li>
 *</ul>
 */
public class ListMaterialLogsBySupplierResponse {
    private Long nextPageAnchor;
    private List<WarehouseLogDTO> dtos;

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

    public List<WarehouseLogDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<WarehouseLogDTO> dtos) {
        this.dtos = dtos;
    }
}
