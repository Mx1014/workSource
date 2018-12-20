package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>equipment: 设备类型</li>
 * <li>activeDoorNumber：已激活门禁数</li>
 * </ul>
 */
public class ActiveDoorByEquipmentDTO {
    private Byte doorType;

    private Integer activeDoorNumber;

    public Byte getDoorType() {
        return doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

    public Integer getActiveDoorNumber() {
        return activeDoorNumber;
    }

    public void setActiveDoorNumber(Integer activeDoorNumber) {
        this.activeDoorNumber = activeDoorNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
