package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>equipment: 设备类型</li>
 * <li>activeDoorNumber：已激活门禁数</li>
 * </ul>
 */
public class ActiveDoorByEquipmentDTO {
    private String equipment;

    private Long activeDoorNumber;

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Long getActiveDoorNumber() {
        return activeDoorNumber;
    }

    public void setActiveDoorNumber(Long activeDoorNumber) {
        this.activeDoorNumber = activeDoorNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
