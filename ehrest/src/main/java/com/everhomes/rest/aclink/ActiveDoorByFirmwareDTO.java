package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>firmware: 固件版本</li>
 * <li>activeDoorNumber：已激活门禁数</li>
 * </ul>
 */
public class ActiveDoorByFirmwareDTO {
    private String firmware;

    private Long activeDoorNumber;

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
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
