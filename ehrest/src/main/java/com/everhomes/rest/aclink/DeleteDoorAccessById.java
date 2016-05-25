package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorAccessId: 可以是组 ID 或者是 门禁设备 ID</li>
 * </ul>
 * @author janson
 *
 */
public class DeleteDoorAccessById {
    Long doorAccessId;

    public Long getDoorAccessId() {
        return doorAccessId;
    }

    public void setDoorAccessId(Long doorAccessId) {
        this.doorAccessId = doorAccessId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
