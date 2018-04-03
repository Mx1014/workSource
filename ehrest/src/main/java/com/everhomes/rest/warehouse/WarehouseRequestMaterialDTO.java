package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>materialId: 物品id</li>
 *     <li>materialName: 物品名称</li>
 *     <li>materialNumber: 物品编码</li>
 *     <li>requestAmount: 申请数量</li>
 *     <li>warehouseId: 仓库id</li>
 *     <li>warehouseName: 仓库名</li>
 *     <li>requestUid: 申请人id</li>
 *     <li>requestUserName: 申请人姓名</li>
 *     <li>stockAmount: 库存数量</li>
 *     <li>reviewResult： 审阅结果 参考{@link com.everhomes.rest.warehouse.WarehouseRequestReviewResult}</li>
 *     <li>deliveryFlag: 出库状态 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialDeliveryFlag}</li>
 *     <li>createTime: 申请时间</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseRequestMaterialDTO {

    private Long id;

    private Long requestId;

    private Long materialId;

    private String materialName;

    private String materialNumber;

    private Long requestAmount;

    private Long warehouseId;

    private String warehouseName;

    private Long requestUid;

    private String requestUserName;

    private Long stockAmount;

    private Byte reviewResult;

    private Byte deliveryFlag;

    private Timestamp createTime;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(Byte deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(Long requestAmount) {
        this.requestAmount = requestAmount;
    }

    public Long getRequestUid() {
        return requestUid;
    }

    public void setRequestUid(Long requestUid) {
        this.requestUid = requestUid;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    public Long getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Long stockAmount) {
        this.stockAmount = stockAmount;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
