//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>materialId:物品id</li>
 * <li>purchaseQuantity:采购数量</li>
 * <li>unitPrice:单价</li>
 * <li>warehouseId:仓库id</li>
 *</ul>
 */
public class PurchaseMaterialDTO {
    private Long materialId;
    private Long purchaseQuantity;
    private String unitPrice;
    private Long warehouseId;

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Long purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
