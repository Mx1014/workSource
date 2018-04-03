package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 物品单位id</li>
 *     <li>ownerType: 物品单位所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品单位所属类型id</li>
 *     <li>name: 物品单位</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class WarehouseMaterialUnitDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
