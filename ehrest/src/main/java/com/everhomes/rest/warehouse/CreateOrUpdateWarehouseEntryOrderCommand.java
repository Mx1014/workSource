//@formatter:off
package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/11.
 */

/**
 *<ul>
 * <li>dtos:新增的物品信息列表，详情见{@link com.everhomes.rest.warehouse.CreateWarehouseEntryOrderDTO}</li>
 * <li>id:出入库单id</li>
 *</ul>
 */
public class CreateOrUpdateWarehouseEntryOrderCommand {
    List<CreateWarehouseEntryOrderDTO> dtos;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<CreateWarehouseEntryOrderDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CreateWarehouseEntryOrderDTO> dtos) {
        this.dtos = dtos;
    }
}
