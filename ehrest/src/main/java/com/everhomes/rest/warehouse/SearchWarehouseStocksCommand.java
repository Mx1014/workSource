package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 库存所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 库存所属类型id</li>
 *     <li>warehouseId: 仓库id</li>
 *     <li>name: 物品名称</li>
 *     <li>materialNumber: 物品编码</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchWarehouseStocksCommand {

    private String ownerType;

    private Long ownerId;

    private Long warehouseId;

    private String name;

    private String materialNumber;

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
