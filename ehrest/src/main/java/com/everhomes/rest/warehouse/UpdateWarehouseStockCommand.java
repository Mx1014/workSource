package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ownerType: 库存所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 库存所属类型id</li>
 *     <li>requestType: 操作类型 参考{@link com.everhomes.rest.warehouse.WarehouseStockRequestType}</li>
 *     <li>stocks: 库存列表 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialStock}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class UpdateWarehouseStockCommand {

    private String ownerType;

    private Long ownerId;

    private Byte requestType;

    private Long requestId;

    @ItemType(WarehouseMaterialStock.class)
    private List<WarehouseMaterialStock> stocks;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Byte getRequestType() {
        return requestType;
    }

    public void setRequestType(Byte requestType) {
        this.requestType = requestType;
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

    public List<WarehouseMaterialStock> getStocks() {
        return stocks;
    }

    public void setStocks(List<WarehouseMaterialStock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
