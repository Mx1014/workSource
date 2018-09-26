// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorType：门禁类型</li>
 * <li>firmwareName：固件名称</li>
 * <li>firmwareId：固件id</li>
 * </ul>
 */
public class ChangeUpdateFirmwareCommand {
    private Byte doorType;
    private String firmwareName;
    private Long firmwareId;

    public Byte getDoorType() {
        return doorType;
    }

    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }

    public String getFirmwareName() {
        return firmwareName;
    }

    public void setFirmwareName(String firmwareName) {
        this.firmwareName = firmwareName;
    }

    public Long getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Long firmwareId) {
        this.firmwareId = firmwareId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
