//@formatter:off
package com.everhomes.rest.warehouse;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

/**
 *<ul>
 * <li>materialNumber:物品编号</li>
 * <li>materialName:物品名称</li>
 * <li>materialCategory:物品分类</li>
 * <li>applicantName:申请人</li>
 * <li>applicationTime:申请时间</li>
 * <li>purchaseQuantity:采购数量</li>
 * <li>unitPrice:单价</li>
 *</ul>
 */
public class WarehouseLogDTO {
    private String materialNumber;
    private String materialName;
    private String materialCategory;
    private String applicantName;
    private String applicationTime;
    private Long purchaseQuantity;
    private String unitPrice;

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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
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
