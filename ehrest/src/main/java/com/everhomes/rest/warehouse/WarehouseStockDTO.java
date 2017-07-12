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
 *     <li>amount: 数量</li>
 *     <li>warehouseName: 仓库名称</li>
 *     <li>materialName: 物品名称</li>
 *     <li>materialNumber: 物品编号</li>
 *     <li>categoryId: 物品分类id</li>
 *     <li>categoryName: 物品分类名称</li>
 *     <li>unitId: 单位id</li>
 *     <li>unitName: 单位名</li>
 *     <li>updateTime: 更新时间</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseStockDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private Long warehouseId;

    private String warehouseName;

    private Long materialId;

    private String materialName;

    private String materialNumber;

    private Long categoryId;

    private String categoryName;

    private Long unitId;

    private String unitName;

    private Long amount;

    private Timestamp updateTime;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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
