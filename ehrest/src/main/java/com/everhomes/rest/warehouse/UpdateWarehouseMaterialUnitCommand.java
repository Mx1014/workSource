package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ownerType: 物品单位所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品单位所属类型id</li>
 *     <li>units: 物品单位信息 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialUnitDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class UpdateWarehouseMaterialUnitCommand {

    private String ownerType;

    private Long ownerId;

    @ItemType(WarehouseMaterialUnitDTO.class)
    private List<WarehouseMaterialUnitDTO> units;

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

    public List<WarehouseMaterialUnitDTO> getUnits() {
        return units;
    }

    public void setUnits(List<WarehouseMaterialUnitDTO> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
