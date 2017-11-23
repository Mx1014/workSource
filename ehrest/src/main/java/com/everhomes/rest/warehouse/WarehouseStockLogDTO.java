package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 库存id</li>
 *     <li>ownerType: 库存所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 库存所属类型id</li>
 *     <li>warehouseId: 仓库id</li>
 *     <li>materialId: 物品id</li>
 *     <li>requestid: 申请id</li>
 *     <li>requestType: 请求类型 参考{@link com.everhomes.rest.warehouse.WarehouseStockRequestType}</li>
 *     <li>requestSource: 请求来源 参考{@link com.everhomes.rest.warehouse.WarehouseStockRequestSource}</li>
 *     <li>stockAmount: 库存数量</li>
 *     <li>deliveryAmount: 交付数量</li>
 *     <li>requestUid: 申请人id</li>
 *     <li>requestUserName: 申请人姓名</li>
 *     <li>deliveryUid: 操作人id</li>
 *     <li>deliveryUserName: 操作人姓名</li>
 *     <li>createTime: 操作时间</li>
 *     <li>warehouseName: 仓库名</li>
 *     <li>materialName: 物品名</li>
 *     <li>materialNumber: 物品编号</li>
 *     <li>unitId: 单位id</li>
 *     <li>unitName: 单位名</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseStockLogDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private Long requestid;

    private Byte requestType;

    private Byte requestSource;

    private Long warehouseId;

    private String warehouseName;

    private Long materialId;

    private String materialName;

    private String materialNumber;

    private Long unitId;

    private String unitName;

    private Long stockAmount;

    private Long deliveryAmount;

    private Long requestUid;

    private String requestUserName;

    private Long deliveryUid;

    private String deliveryUserName;

    private Timestamp createTime;

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Long deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public Long getDeliveryUid() {
        return deliveryUid;
    }

    public void setDeliveryUid(Long deliveryUid) {
        this.deliveryUid = deliveryUid;
    }

    public String getDeliveryUserName() {
        return deliveryUserName;
    }

    public void setDeliveryUserName(String deliveryUserName) {
        this.deliveryUserName = deliveryUserName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getRequestid() {
        return requestid;
    }

    public void setRequestid(Long requestid) {
        this.requestid = requestid;
    }

    public Byte getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(Byte requestSource) {
        this.requestSource = requestSource;
    }

    public Byte getRequestType() {
        return requestType;
    }

    public void setRequestType(Byte requestType) {
        this.requestType = requestType;
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

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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
