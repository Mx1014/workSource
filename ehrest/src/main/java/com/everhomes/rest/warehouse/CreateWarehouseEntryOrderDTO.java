//@formatter:off
package com.everhomes.rest.warehouse;

/**
 * Created by Wentian Wang on 2018/1/11.
 */
/**
 *<ul>
 * <li>materialId:物品id</li>
 * <li>warehouseId:仓库id</li>
 * <li>quantity:入库数量</li>
 *</ul>
 */
public class CreateWarehouseEntryOrderDTO {
    private Long materialId;
    private Long warehouseId;
    private Long quantity;

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
