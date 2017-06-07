package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>warehouseId: 仓库id</li>
 *     <li>materialId: 物品id</li>
 *     <li>amount: 变化数量</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseMaterialStock {

    private Long warehouseId;

    private Long materialId;

    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
