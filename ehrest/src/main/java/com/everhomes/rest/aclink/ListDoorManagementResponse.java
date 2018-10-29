// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * <li> doors: 门禁设备列表，参考{@link DoorAccessNewDTO}</li>
 * </ul>
 */
public class ListDoorManagementResponse {


    @ItemType(AclinkManagementDTO.class)
    private List<AclinkManagementDTO> doors;

    public List<AclinkManagementDTO> getDoors() {
        return doors;
    }

    public void setDoors(List<AclinkManagementDTO> doors) {
        this.doors = doors;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
