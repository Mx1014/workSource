package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 获取最新的固件
 * <li></li>
 * </ul>
 * @author janson
 *
 */
public class GetCurrentFirmwareCommand {
    private String firmwareType;

    public String getFirmwareType() {
        return firmwareType;
    }

    public void setFirmwareType(String firmwareType) {
        this.firmwareType = firmwareType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
