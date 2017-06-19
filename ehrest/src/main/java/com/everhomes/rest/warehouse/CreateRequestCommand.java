package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ownerType: 库存所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 库存所属类型id</li>
 *     <li>requestOrganizationId: 申请人所属机构id</li>
 *     <li>remark: 申请说明</li>
 *     <li>requestType: 申请类型 参考{@link com.everhomes.rest.warehouse.WarehouseStockRequestType}</li>
 *     <li>stocks: 库存列表 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialStock}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class CreateRequestCommand {

    private String ownerType;

    private Long ownerId;

    private Long requestOrganizationId;

    private String remark;

    private Byte requestType;

    @ItemType(WarehouseMaterialStock.class)
    private List<WarehouseMaterialStock> stocks;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRequestOrganizationId() {
        return requestOrganizationId;
    }

    public void setRequestOrganizationId(Long requestOrganizationId) {
        this.requestOrganizationId = requestOrganizationId;
    }

    public Byte getRequestType() {
        return requestType;
    }

    public void setRequestType(Byte requestType) {
        this.requestType = requestType;
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
