package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 仓库所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 仓库所属类型id</li>
 *     <li>name: 仓库名称</li>
 *     <li>status: 仓库状态 参考{@link com.everhomes.rest.warehouse.WarehouseStatus}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/10.
 */
public class SearchWarehousesCommand {

    private String ownerType;

    private Long ownerId;

    private Byte status;

    private String name;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
