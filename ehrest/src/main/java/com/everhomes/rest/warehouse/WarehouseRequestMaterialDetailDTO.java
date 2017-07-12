package com.everhomes.rest.warehouse;


import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>ownerType: 所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 所属类型id</li>
 *     <li>requestId: 申请id</li>
 *     <li>categoryName: 物品分类</li>
 *     <li>materialId: 物品id</li>
 *     <li>materialName: 物品名称</li>
 *     <li>materialNumber: 物品编码</li>
 *     <li>deliveryAmount: 申请数量</li>
 *     <li>stockAmount: 库存数</li>
 *     <li>warehouseId: 仓库id</li>
 *     <li>warehouseName: 仓库名</li>
 *     <li>requestUid: 申请人id</li>
 *     <li>requestUserName: 申请人姓名</li>
 *     <li>reviewResult： 审阅结果 参考{@link com.everhomes.rest.warehouse.WarehouseRequestReviewResult}</li>
 *     <li>deliveryTime： 出库时间</li>
 *     <li>deliveryFlag: 出库状态 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialDeliveryFlag}</li>
 *     <li>brand: 品牌</li>
 *     <li>itemNo: 型号</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseRequestMaterialDetailDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private Long requestId;

    private Long requestUid;

    private String requestUserName;

    private Long materialId;

    private String materialName;

    private String materialNumber;

    private String categoryName;

    private String brand;

    private String itemNo;

    private Long deliveryAmount;

    private Long stockAmount;

    private String warehouseName;

    private Long warehouseId;

    private Timestamp deliveryTime;

    private Byte deliveryFlag;

    private Byte reviewResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Long deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public Byte getDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(Byte deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public Long getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Long stockAmount) {
        this.stockAmount = stockAmount;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestUid() {
        return requestUid;
    }

    public void setRequestUid(Long requestUid) {
        this.requestUid = requestUid;
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
