//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>materialId:物品id</li>
 * <li>purchaseQuantity:采购数量</li>
 * <li>unitPrice:单价</li>
 * <li>supplierName:供应商名字</li>
 * <li>materialNumber:物品编码</li>
 * <li>materialName:物品名称</li>
 * <li>materialCategory:物品分类</li>
 * <li>belongedWarehouse:所属仓库</li>
 * <li>unit:单位</li>
 * <li>stock:库存</li>
 * <li>totalAmount:总金额</li>
 *</ul>
 */
public class PurchaseMaterialDetailDTO {
    private Long materialId;
    private Long purchaseQuantity;
    private String unitPrice;
    private String supplierName;
    private String materialNumber;
    private String materialName;
    private String materialCategory;
    private String belongedWarehouse;
    private String unit;
    private Long stock;
    private String totalAmount;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCategory() {
        return materialCategory;
    }

    public void setMaterialCategory(String materialCategory) {
        this.materialCategory = materialCategory;
    }

    public String getBelongedWarehouse() {
        return belongedWarehouse;
    }

    public void setBelongedWarehouse(String belongedWarehouse) {
        this.belongedWarehouse = belongedWarehouse;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
