// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 添加临时授权优先门禁。
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type：0园区 1企业</li>
 * <li>List<Long> doorIds: 门禁or门禁组列表</li>
 * </ul>
 *
 */
public class CreateTempAuthPriorityCommand {

    private Long ownerId;
    private Byte ownerType;
//    private List<Long> doorIds;
    @ItemType(DoorsAndGroupsDTO.class)
    private List<DoorsAndGroupsDTO> doors;

    public List<DoorsAndGroupsDTO> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorsAndGroupsDTO> doors) {
        this.doors = doors;
    }

//    public List<Long> getDoorIds() {
//        return doorIds;
//    }
//
//    public void setDoorIds(List<Long> doorIds) {
//        this.doorIds = doorIds;
//    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
