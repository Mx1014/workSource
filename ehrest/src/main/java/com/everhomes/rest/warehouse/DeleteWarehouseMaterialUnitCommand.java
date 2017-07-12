package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 物品单位id</li>
 *     <li>ownerType: 物品单位所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品单位所属类型id</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class DeleteWarehouseMaterialUnitCommand {

    private Long id;

    private String ownerType;

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
