package com.everhomes.rest.aclink;

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
    
    
}
